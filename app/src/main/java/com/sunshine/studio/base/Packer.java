package com.sunshine.studio.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;

import com.sunshine.engine.base.Render2D;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jack on 2019-11-11.
 */
public class Packer {
	public static int getArea(List<Cell> lstRc, Cell rcNew) {
		int[] ary = getWH(lstRc, rcNew);
		return ary[0] * ary[1];
	}

	public static int[] getWH(List<Cell> lstRc) {
		return getWH(lstRc, null);
	}

	public static int[] getWH(List<Cell> lstRc, Cell rcNew) {
		int w = rcNew == null ? 0 : rcNew.right();
		int h = rcNew == null ? 0 : rcNew.bottom();
		for (Cell rc : lstRc) {
			w = Math.max(w, rc.right());
			h = Math.max(h, rc.bottom());
		}
		return new int[]{w, h};
	}

	public static List<Cell> getLstBmpRc(String folderPath) {
		List<Cell> lst = new ArrayList<>();
		String[] aryFiles = new File(folderPath).list((dir, name) -> {
			name = name.toLowerCase();
			return name.endsWith(".png") || name.endsWith(".jgp") || name.endsWith(".jpeg");
		});
		if (aryFiles != null && aryFiles.length > 0) {
			Arrays.sort(aryFiles, String.CASE_INSENSITIVE_ORDER);
			for (String name : aryFiles) {
				lst.add(new Cell(name, folderPath + File.separator + name));
			}
		}
		return lst;
	}

	public static void saveFiles(List<Cell> lstRc, String folderPath) {
		// file
		File file;
		String picPath = folderPath + File.separator + "pic";
		String plistPath = folderPath + File.separator + "pic.plist";
		// build pic
		int[] ary = Packer.getWH(lstRc);
		Bitmap bmp = Bitmap.createBitmap(ary[0], ary[1], Bitmap.Config.ARGB_8888);
		Canvas can = new Canvas(bmp);
		Render2D.setDrawFilter(can);
		for (Cell rc : lstRc) {
			can.drawBitmap(rc.bmp, rc.getRcPic(), rc.getDst(), Render2D.PAINT);
			rc.bmp.recycle();
		}
		// save pic
		BufferedOutputStream bos = null;
		file = new File(picPath);
		if (file.exists()) {
			file.delete();
		}
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
			bos.flush();
		} catch (Exception e) {
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
			}
		}
		bmp.recycle();
		// save plist
		file = new File(plistPath);
		if (file.exists()) {
			file.delete();
		}
		XmlWriter.save(
				plistPath,
				new PlistWriter(lstRc));
	}

	public static List<Cell> packer(List<Cell> lstRc) {
		// 找出最大边 & 排序
		for (Cell rc : lstRc) {
			rc.max = Math.max(rc.width(), rc.height());
		}
		Collections.sort(
				lstRc,
				(a, b) -> {
					if (a.max == b.max) {
						return b.width() * b.height() - a.width() * a.height();
					} else {
						return b.max - a.max;
					}
				});
		// 安置Rc
		List<Cell> lstResult = new ArrayList<>();
		for (Cell rc : lstRc) {
			if (lstResult.size() == 0) {
				lstResult.add(rc);
			} else {
				build(lstResult, rc);
			}
		}
		return lstResult;
	}

	private static void build(List<Cell> lstRc, Cell rcBmp) {
		// 收集可以作为新增矩形的坐标，即矩形左上角的顶点
		List<Anchor> lstPt = new ArrayList<>();
		for (Cell rc : lstRc) {
			// -lt
			lstPt.remove(new Anchor(rc.left(), rc.top()));
			// -rb
			lstPt.remove(new Anchor(rc.right(), rc.bottom()));

			int x, y;
			// +rt -> y
			x = rc.right();
			y = rc.top();
			int top = -1;
			for (Cell r : lstRc) {
				if (r.left() <= x && r.right() > x && r.bottom() <= y) {
					top = Math.max(top, r.bottom());
				}
			}
			if (top != -1) {
				lstPt.remove(new Anchor(x, y));
				y = Math.min(y, top);
			}
			lstPt.add(new Anchor(x, y));
			// +lb -> x
			x = rc.left();
			y = rc.bottom();
			int left = -1;
			for (Cell r : lstRc) {
				if (r.top() <= y && r.bottom() > y && x >= r.right()) {
					left = Math.max(left, r.right());
				}
			}
			if (left != -1) {
				lstPt.remove(new Anchor(x, y));
				x = Math.min(x, left);
			}
			lstPt.add(new Anchor(x, y));
		}
		// 计算这个点能放入的最大尺寸矩形 & 删除掉已被贴边的左边 & 如果是孔位则权值++
		Iterator<Anchor> iterator = lstPt.iterator();
		while (iterator.hasNext()) {
			Anchor pt = iterator.next();
			for (Cell rc : lstRc) {
				// pt在rc左边
				if (rc.top() <= pt.y && pt.y < rc.bottom()) {
					if (pt.x < rc.left()) {
						pt.maxW = Math.min(pt.maxW, rc.left() - pt.x);
						pt.weight++;
					} else if (pt.x == rc.left()) {
						iterator.remove();
						break;
					}
				}
				// pt在rc上边
				if (pt.x >= rc.left() && pt.x < rc.right()) {
					if (pt.y < rc.top()) {
						pt.maxH = Math.min(pt.maxH, rc.top() - pt.y);
						pt.weight++;
					} else if (pt.y == rc.top()) {
						iterator.remove();
						break;
					}
				}
			}
		}
		// 过滤出可以放入新rc的点 & 计算面积
		iterator = lstPt.iterator();
		while (iterator.hasNext()) {
			Anchor pt = iterator.next();
			if (pt.maxW < rcBmp.width() || pt.maxH < rcBmp.height()) {
				iterator.remove();
			} else {
				rcBmp.setXY(pt.x, pt.y);
				pt.area = getArea(lstRc, rcBmp);
			}
		}
		// 筛出来面积最小
		List<Anchor> lstMinArea = new ArrayList<>();
		for (Anchor pt : lstPt) {
			if (lstMinArea.size() == 0) {
				lstMinArea.add(pt);
			} else {
				if (pt.area == lstMinArea.get(0).area) {
					lstMinArea.add(pt);
				} else if (pt.area < lstMinArea.get(0).area) {
					lstMinArea.clear();
					lstMinArea.add(pt);
				}
			}
		}
		// 排序最优解，即面积最小 & 在宽高有限的区域填入新增矩形则认为最优
		Collections.sort(lstMinArea, (a, b) -> b.weight - a.weight);
		Anchor ptBest = lstMinArea.get(0);
		rcBmp.setXY(ptBest.x, ptBest.y);
		lstRc.add(rcBmp);
	}

	public static class Anchor {
		private int x = 0;
		private int y = 0;

		private int maxW = Integer.MAX_VALUE;
		private int maxH = Integer.MAX_VALUE;
		private int area = 0;
		private byte weight = 0;

		public Anchor(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Anchor) {
				Anchor pt = (Anchor) obj;
				return this.x == pt.x && this.y == pt.y;
			} else {
				return false;
			}
		}
	}

	public static class Cell {
		protected Bitmap bmp = null;
		protected String name = null;
		private Rect rcPic = new Rect(); // bmp内部非alpha部分的区域
		private Point pt = new Point();

		private static final byte SPACE = 1;

		public Cell(String name, String path) {
			int n = name.lastIndexOf(".");
			if (n > -1) {
				this.name = name.substring(0, n);
			} else {
				this.name = name;
			}
			bmp = BitmapFactory.decodeFile(path);
			decodeBmp();
		}

		public void setXY(int x, int y) {
			pt.x = x;
			pt.y = y;
		}

		private void decodeBmp() {
			int x, y;
			boolean out = false;
			// 从上往下记录有颜色的起始点
			for (y = 0; y < bmp.getHeight(); y++) {
				for (x = 0; x < bmp.getWidth(); x++) {
					if (Color.alpha(bmp.getPixel(x, y)) > 0) {
						out = true;
						break;
					}
				}
				if (out) {
					out = false;
					break;
				}
			}
			rcPic.top = y;
			// 从下往上记录有颜色的起始点
			for (y = bmp.getHeight() - 1; y > rcPic.top; y--) {
				for (x = 0; x < bmp.getWidth(); x++) {
					if (Color.alpha(bmp.getPixel(x, y)) > 0) {
						out = true;
						break;
					}
				}
				if (out) {
					out = false;
					break;
				}
			}
			rcPic.bottom = y + 1;
			// 从左往右记录有颜色的起始点
			for (x = 0; x < bmp.getWidth(); x++) {
				for (y = rcPic.top; y < rcPic.bottom; y++) {
					if (Color.alpha(bmp.getPixel(x, y)) > 0) {
						out = true;
						break;
					}
				}
				if (out) {
					out = false;
					break;
				}
			}
			rcPic.left = x;
			// 从右往左记录有颜色的起始点
			for (x = bmp.getWidth() - 1; x > rcPic.left; x--) {
				for (y = rcPic.top; y < rcPic.bottom; y++) {
					if (Color.alpha(bmp.getPixel(x, y)) > 0) {
						out = true;
						break;
					}
				}
				if (out) {
					out = false;
					break;
				}
			}
			rcPic.right = x + 1;
		}

		public Rect getDst() {
			return new Rect(pt.x, pt.y, pt.x + rcPic.width(), pt.y + rcPic.height());
		}

		public Rect getRcPic() {
			return rcPic;
		}

		private int max = 0;


		public int left() {
			return pt.x;
		}

		public int right() {
			return pt.x + rcPic.width() + SPACE;
		}

		public int top() {
			return pt.y;
		}

		public int bottom() {
			return pt.y + rcPic.height() + SPACE;
		}

		public int width() {
			return rcPic.width() + SPACE;
		}

		public int height() {
			return rcPic.height() + SPACE;
		}

		public int picWidth() {
			return rcPic.width();
		}

		public int picHeight() {
			return rcPic.height();
		}
	}
}
