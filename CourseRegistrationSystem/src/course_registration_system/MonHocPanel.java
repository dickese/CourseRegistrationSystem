package course_registration_system;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import daos.MonHocDAO;
import pojo.MonHoc;

public class MonHocPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	ListPanel listPanel;
	InfoPanel infoPanel;

	public MonHocPanel() {
		this.setLayout(new GridBagLayout());
		GBCBuilder gbc = new GBCBuilder(1, 1);

		// ------------------------ INFO PANEL --------------------------------
		infoPanel = new InfoPanel(new String[] { "Mã môn học: ", "Tên môn học: ", "Số tín chỉ: " },
				new String[] { "Cập nhật thông tin môn học", "Xoá môn học" }, this);
		infoPanel.updateInfo();

		// ------------------------ LIST PANEL --------------------------------

		listPanel = new ListPanel(infoPanel, MonHocDAO.getObjectMatrix(),
				new String[] { "Mã môn học", "Tên môn học", "Số tín chỉ" }, new String[] { "Thêm môn học" }, this,
				"môn học");

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listPanel, infoPanel);
		this.add(splitPane, gbc.setGrid(1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setInsets(0));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {

		case "Thêm môn học": {
			EnterInputDialog themMonHocDialog = new EnterInputDialog(
					new String[] { "Mã môn học", "Tên môn học", "Số tín chỉ" },
					new JComponent[] { new JTextField(), new JTextField(),
							new JComboBox<String>(new String[] { "1", "2", "3", "4" }) },
					new String[4], "Thêm môn học");
			String[] thongTinMonHocMoi = themMonHocDialog.showDialog();
			if (thongTinMonHocMoi == null)
				break;

			if (MonHocDAO.layThongTinMonHoc(thongTinMonHocMoi[0]) != null)
				JOptionPane.showMessageDialog(this, "Môn học đã tồn tại", "Lỗi thêm môn học", JOptionPane.ERROR_MESSAGE,
						null);
			else {

				try {

					MonHoc newMonHoc = new MonHoc();
					newMonHoc.setMaMH(thongTinMonHocMoi[0]);
					newMonHoc.setTenMH(thongTinMonHocMoi[1]);
					newMonHoc.setSoTinChi(Integer.parseInt(thongTinMonHocMoi[2]));

					MonHocDAO.themMonHoc(newMonHoc);

					listPanel.updateTable(MonHocDAO.getObjectMatrix());
					listPanel.setTableSelectedRow(listPanel.theTable.getRowCount() - 1);

					JOptionPane.showMessageDialog(this, "Thêm môn học thành công!", "Thông báo",
							JOptionPane.INFORMATION_MESSAGE, null);
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(this, "Số tín chỉ phải là 1 số nguyên", "Lỗi thêm môn học",
							JOptionPane.WARNING_MESSAGE, null);
				}
			}
			break;
		}
		case "Cập nhật thông tin môn học": {
			String monHocCanCapNhat = infoPanel.elementDatas[0];
			MonHoc selectedMonHoc = MonHocDAO.layThongTinMonHoc(monHocCanCapNhat);

			EnterInputDialog capNhatThongTinDialog;
			capNhatThongTinDialog = new EnterInputDialog(new String[] { "Tên môn học", "Số tín chỉ" },
					new JComponent[] { new JTextField(), new JComboBox<String>(new String[] { "1", "2", "3", "4" }) },
					new String[] { selectedMonHoc.getTenMH(), "" + selectedMonHoc.getSoTinChi() },
					"Cập nhật thông tin");

			String[] result = capNhatThongTinDialog.showDialog();
			if (result != null) {
				try {
					selectedMonHoc.setTenMH(result[0]);
					selectedMonHoc.setSoTinChi(Integer.parseInt(result[1]));

					MonHocDAO.capNhatThongTinMonHoc(selectedMonHoc);
					listPanel.updateTable(MonHocDAO.getObjectMatrix());

					JOptionPane.showMessageDialog(capNhatThongTinDialog, "Cập nhật thông tin thành công!", "Thông báo",
							JOptionPane.INFORMATION_MESSAGE, null);

				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(capNhatThongTinDialog, "Sai định dạng", "Lỗi cập nhật thông tin",
							JOptionPane.WARNING_MESSAGE, null);
				}
			}

			break;
		}
		case "Xoá môn học": {
			String monHocCanXoa = listPanel.theTable.getValueAt(listPanel.theTable.getSelectedRow(), 0).toString();

			if (JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xoá môn học " + monHocCanXoa, "Xoá môn học",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

				MonHocDAO.xoaMonHoc(monHocCanXoa);

				listPanel.updateTable(MonHocDAO.getObjectMatrix());

				JOptionPane.showMessageDialog(this, "Xoá môn học thành công!", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE, null);
			}
			break;
		}
		}
	}
}
