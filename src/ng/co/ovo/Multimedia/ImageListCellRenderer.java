package ng.co.ovo.Multimedia;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class ImageListCellRenderer implements ListCellRenderer<Object> {

	@Override
	public Component getListCellRendererComponent(JList<?> jList,
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (value instanceof JPanel ){
			Component component = (Component) value;
			component.setForeground(Color.BLACK);
			return component;
		}else{
			return new JLabel();
		}
	}

}
