// https://blogs.oracle.com/jfxprg/entry/dynamical_layouts_in_fx

package sinelayout;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class SinePane extends Pane {

	private double spacing = 0;

	/**
	 * @todo: b) Explain why requestLayout is called in this method.
	 *        The requestLayout() method is a method provided by the Pane class,
	 *        which is the superclass of the SinePane class.
	 *        It is used to request a layout pass for the Pane and its children.
	 *        calling requestLayout() in the setSpacing(double s) method ensures
	 *        that the layout of the SinePane is updated
	 *        when the spacing value is changed.
	 * @param s
	 */
	public void setSpacing(double s) {
		spacing = s;
		requestLayout();
	}

	public double getSpacing() {
		return spacing;
	}

	@Override
	protected double computeMinHeight(double forWidth) {
		// double v = super.computeMinHeight(forWidth);
		// System.out.println("computeMinHeight(" + forWidth + ") = " + v);
		return computePrefHeight(forWidth);
	}

	@Override
	protected double computeMinWidth(double forHeight) {
		// double v = super.computeMinWidth(forHeight);
		// System.out.println("computeMinWidth(" + forHeight + ") = " + v);
		return computePrefWidth(forHeight);
	}

	/**
	 * @todo d) Explain how the preferred height of the pane is computed.
	 * 
	 *       The computePrefHeight(double forWidth) method calculates the preferred
	 *       height of the pane.
	 *       It takes into account the top and bottom insets of the pane, the sum of
	 *       preferred heights
	 *       of the child nodes, and the total spacing between the child nodes. The
	 *       preferred height is
	 *       the sum of these values.
	 * 
	 * @param forWidth The width to compute the preferred height for.
	 * @return The preferred height of the pane.
	 */
	@Override
	protected double computePrefHeight(double forWidth) {
		// double v = super.computePrefHeight(forWidth);
		// System.out.println("computePrefHeight(" + forWidth + ") = " + v);
		Insets i = getInsets();
		return i.getTop() +
				sumPrefHeightChildren() +
				sumSpacing() +
				i.getBottom();
	}

	/**
	 * @todo d) Explain how the preferred width of the pane is computed.
	 * 
	 *       The computePrefWidth(double forHeight) method calculates the preferred
	 *       width of the pane.
	 *       It takes into account the left and right insets of the pane, the
	 *       maximum preferred width
	 *       of the child nodes, and the total width of the pane. The preferred
	 *       width is the sum of
	 *       these values.
	 * 
	 * @param forHeight The height to compute the preferred width for.
	 * @return The preferred width of the pane.
	 */
	@Override
	protected double computePrefWidth(double forHeight) {
		// double v = super.computePrefWidth(forHeight);
		// System.out.println("computePrefWidth(" + forHeight + ") = " + v);
		Insets i = getInsets();
		return i.getLeft() +
				maxPrefWidthChildren() +
				i.getRight();
	}

	@Override
	protected double computeMaxHeight(double forWidth) {
		// double v = super.computeMaxHeight(forWidth);
		// System.out.println("computePrefWidth(" + forWidth + ") = " + v);
		return computePrefHeight(forWidth);
	}

	/**
	 * @todo e) What would happen if max width was same as preferred width?
	 * 
	 *       If the maximum width is set to be the same as the preferred width,
	 *       it means that the layout system will not impose any constraints on the
	 *       width of the node.
	 *       The node will be allowed to expand horizontally as much as it wants, up
	 *       to the preferred width.
	 *       This can result in the node taking up more space than necessary and
	 *       potentially causing layout issues if there
	 *       are other nodes in the layout that need to be positioned accordingly.
	 * 
	 * @param forHeight The height to compute the maximum width for.
	 * @return The maximum width of the node. In this case, it returns
	 *         Double.MAX_VALUE to indicate that there is no
	 *         maximum width constraint.
	 */
	@Override
	protected double computeMaxWidth(double forHeight) {
		// double v = super.computeMaxWidth(forHeight);
		// System.out.println("computePrefWidth(" + forHeight + ") = " + v);
		// return computePrefWidth(forHeight);
		return Double.MAX_VALUE;
	}

	protected double prefWidth(Node node) {
		double pw, ph;
		if (node.getContentBias() == Orientation.HORIZONTAL) {
			pw = node.prefWidth(-1);
		} else if (node.getContentBias() == Orientation.VERTICAL) {
			ph = node.prefHeight(-1);
			pw = node.prefWidth(ph);
		} else { // no content bias
			pw = node.prefWidth(-1);
		}
		return pw;
	}

	protected double prefHeight(Node node) {
		double pw, ph;
		if (node.getContentBias() == Orientation.HORIZONTAL) {
			pw = node.prefWidth(-1);
			ph = node.maxHeight(pw);
		} else if (node.getContentBias() == Orientation.VERTICAL) {
			ph = node.prefHeight(-1);
		} else { // no content bias
			ph = node.prefHeight(-1);
		}
		return ph;
	}

	/**
	 * @todo c) Explain how the x-value is computed from the y-value.
	 *       The waveX method calculates the x-value based on the y-value by
	 *       considering the available width,
	 *       the maximum preferred width of the child nodes,
	 *       the preferred heights of the child nodes, the spacing between the child
	 *       nodes,
	 *       and the angle derived from the y-value and the total height of the
	 *       child nodes.
	 * @param y
	 * @return
	 */
	protected double waveX(double y) {
		Insets ins = getInsets();
		double w = getWidth() - ins.getLeft() - ins.getRight();
		double wm = maxPrefWidthChildren();
		double h = sumPrefHeightChildren() + sumSpacing();
		double theta = 2 * Math.PI * y / h;
		return ins.getLeft() + 0.5 * w +
				(0.5 * w - 0.5 * wm) * Math.sin(theta);
	}

	/**
	 * @todo f) Write a documentation string for this method.
	 *       Returns the maximum preferred width among the managed children nodes of
	 *       this container.
	 * 
	 *       This method iterates through the managed children nodes of the
	 *       container and calculates
	 *       the preferred width for each node using the prefWidth method. It then
	 *       returns the maximum
	 *       preferred width found among all the children nodes.
	 * 
	 * @return The maximum preferred width among the managed children nodes. Returns
	 *         0 if there
	 *         are no managed children nodes or if their preferred widths are not
	 *         defined.
	 */
	protected double maxPrefWidthChildren() {
		double max = 0;
		List<Node> children = getManagedChildren();
		for (int i = 0, n = children.size(); i < n; i++) {
			Node node = children.get(i);
			double w = prefWidth(node);
			max = Math.max(max, w);
		}
		return max;
	}

	/**
	 * @todo f) Write a documentation string for this method.
	 *       Calculates the sum of preferred heights among the managed children
	 *       nodes of this container.
	 * 
	 *       This method iterates through the managed children nodes of the
	 *       container and accumulates
	 *       their preferred heights using the prefHeight method. It then returns
	 *       the total sum of
	 *       preferred heights of all the children nodes.
	 * 
	 * @return The sum of preferred heights among the managed children nodes.
	 *         Returns 0 if there
	 *         are no managed children nodes or if their preferred heights are not
	 *         defined.
	 */
	protected double sumPrefHeightChildren() {
		double sum = 0;
		List<Node> children = getManagedChildren();
		for (int i = 0, n = children.size(); i < n; i++) {
			Node node = children.get(i);
			sum += prefHeight(node);
		}
		return sum;
	}

	/**
	 * @todo f) Write a documentation string for this method.
	 *       Calculates the total spacing between managed children nodes of this
	 *       container.
	 * 
	 *       This method computes the total spacing between the managed children
	 *       nodes by
	 *       multiplying the spacing value by the number of managed children nodes
	 *       minus one.
	 *       The spacing is applied between adjacent children nodes. If the number
	 *       of children
	 *       is less than or equal to one, no spacing is added.
	 * 
	 * @return The total spacing between managed children nodes. Returns 0 if there
	 *         are
	 *         no managed children nodes or if the spacing value is negative.
	 */
	protected double sumSpacing() {
		int n = getManagedChildren().size();
		return Math.max(0, spacing * (n - 1));
	}

	/**
	 * @todo g) Implement this method.
	 */
	@Override
	protected void layoutChildren() {
		List<Node> children = getManagedChildren();
		int n = children.size();
		if (n > 0) {
			// Set layout bounds of each node to preferred width and height
			for (Node node : children) {
				node.autosize();
			}

			Insets ins = getInsets();
			double x, y = ins.getTop();

			// Update node positions to appear on the sine wave
			for (int i = 0; i < n; i++) {
				Node node = children.get(i);
				double nodeWidth = node.prefWidth(-1); // -1 means use current height
				double nodeHeight = node.prefHeight(nodeWidth);

				x = waveX(y) - nodeWidth / 2; // Center the node horizontally on the wave
				y += nodeHeight + spacing; // Move y to the next position below the current node

				node.setLayoutX(x);
				node.setLayoutY(y - nodeHeight); // Set y position to top edge of the node
			}
		}
	}
}
