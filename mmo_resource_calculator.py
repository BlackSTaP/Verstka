import sys
import os
import xml.etree.ElementTree as ET
from PyQt6.QtWidgets import (
    QApplication, QWidget, QVBoxLayout, QHBoxLayout,
    QTableWidget, QTableWidgetItem, QPushButton,
    QFileDialog, QInputDialog
)
from PyQt6.QtGui import QIcon
from PyQt6.QtCore import Qt


class ResourceCalculator(QWidget):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("MMO Resource Calculator")

        self.columns = ["Name", "Count", "Image"]

        # Layouts
        main_layout = QVBoxLayout(self)
        button_layout = QHBoxLayout()
        main_layout.addLayout(button_layout)

        # Buttons
        self.add_column_btn = QPushButton("Add Column")
        self.add_row_btn = QPushButton("Add Row")
        self.save_btn = QPushButton("Save")
        self.load_btn = QPushButton("Load")
        for btn in (self.add_column_btn, self.add_row_btn, self.save_btn, self.load_btn):
            button_layout.addWidget(btn)

        # Table
        self.table = QTableWidget(0, len(self.columns))
        self.table.setHorizontalHeaderLabels(self.columns)
        self.table.cellDoubleClicked.connect(self.handle_double_click)
        main_layout.addWidget(self.table)

        # Connect signals
        self.add_column_btn.clicked.connect(self.add_column)
        self.add_row_btn.clicked.connect(self.add_row)
        self.save_btn.clicked.connect(self.save_table)
        self.load_btn.clicked.connect(self.load_table)

    def add_column(self):
        name, ok = QInputDialog.getText(self, "Add Column", "Column name:")
        if ok and name and name not in self.columns:
            self.columns.append(name)
            col_index = self.table.columnCount()
            self.table.insertColumn(col_index)
            self.table.setHorizontalHeaderItem(col_index, QTableWidgetItem(name))

    def add_row(self):
        row_index = self.table.rowCount()
        self.table.insertRow(row_index)

    def handle_double_click(self, row: int, column: int):
        if self.columns[column] == "Image":
            file, _ = QFileDialog.getOpenFileName(
                self, "Select Image", "", "Images (*.png *.jpg *.jpeg *.bmp *.gif)"
            )
            if file:
                item = QTableWidgetItem()
                item.setData(Qt.ItemDataRole.UserRole, file)
                item.setIcon(QIcon(file))
                self.table.setItem(row, column, item)

    def save_table(self):
        file, _ = QFileDialog.getSaveFileName(self, "Save Table", "", "XML Files (*.xml)")
        if not file:
            return
        root = ET.Element("Table")
        root.set("columns", ",".join(self.columns))
        for r in range(self.table.rowCount()):
            row_elem = ET.SubElement(root, "Row")
            for c, name in enumerate(self.columns):
                cell_elem = ET.SubElement(row_elem, name)
                item = self.table.item(r, c)
                if item:
                    if name == "Image":
                        cell_elem.text = item.data(Qt.ItemDataRole.UserRole) or ""
                    else:
                        cell_elem.text = item.text()
                else:
                    cell_elem.text = ""
        ET.ElementTree(root).write(file, encoding="utf-8", xml_declaration=True)

    def load_table(self):
        file, _ = QFileDialog.getOpenFileName(self, "Load Table", "", "XML Files (*.xml)")
        if not file:
            return
        tree = ET.parse(file)
        root = tree.getroot()
        columns = root.get("columns", "").split(",")
        if columns:
            self.columns = columns
        self.table.setColumnCount(len(self.columns))
        self.table.setHorizontalHeaderLabels(self.columns)
        self.table.setRowCount(0)
        for row_elem in root.findall("Row"):
            row_idx = self.table.rowCount()
            self.table.insertRow(row_idx)
            for c, name in enumerate(self.columns):
                cell_elem = row_elem.find(name)
                if cell_elem is not None:
                    text = cell_elem.text or ""
                    if name == "Image" and os.path.isfile(text):
                        item = QTableWidgetItem()
                        item.setData(Qt.ItemDataRole.UserRole, text)
                        item.setIcon(QIcon(text))
                        self.table.setItem(row_idx, c, item)
                    else:
                        self.table.setItem(row_idx, c, QTableWidgetItem(text))


def main():
    app = QApplication(sys.argv)
    window = ResourceCalculator()
    window.show()
    sys.exit(app.exec())


if __name__ == "__main__":
    main()
