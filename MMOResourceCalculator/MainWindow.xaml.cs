using Microsoft.VisualBasic;
using Microsoft.Win32;
using System.Data;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;

namespace MMOResourceCalculator
{
    public partial class MainWindow : Window
    {
        private DataTable _table = new DataTable();

        public MainWindow()
        {
            InitializeComponent();
            InitializeTable();
            BuildGridColumns();
            ResourceGrid.ItemsSource = _table.DefaultView;
        }

        private void InitializeTable()
        {
            _table.Columns.Add("Name");
            _table.Columns.Add("Count", typeof(int));
            _table.Columns.Add("Image");
        }

        private void BuildGridColumns()
        {
            ResourceGrid.Columns.Clear();
            foreach (DataColumn col in _table.Columns)
            {
                if (col.ColumnName == "Image")
                {
                    var tcol = new DataGridTemplateColumn();
                    tcol.Header = col.ColumnName;
                    tcol.CellTemplate = (DataTemplate)Resources["ImageCellTemplate"];
                    tcol.SortMemberPath = col.ColumnName;
                    ResourceGrid.Columns.Add(tcol);
                }
                else
                {
                    var txt = new DataGridTextColumn();
                    txt.Header = col.ColumnName;
                    txt.Binding = new Binding($"[{col.ColumnName}]");
                    ResourceGrid.Columns.Add(txt);
                }
            }
        }

        private void AddColumnButton_Click(object sender, RoutedEventArgs e)
        {
            string? name = Interaction.InputBox("Column name", "Add Column", "NewColumn");
            if (!string.IsNullOrEmpty(name) && !_table.Columns.Contains(name))
            {
                _table.Columns.Add(name);
                BuildGridColumns();
            }
        }

        private void AddRowButton_Click(object sender, RoutedEventArgs e)
        {
            _table.Rows.Add(_table.NewRow());
        }

        private void ResourceGrid_MouseDoubleClick(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            if (ResourceGrid.CurrentColumn is DataGridTemplateColumn && ResourceGrid.CurrentColumn.DisplayIndex == GetImageColumnIndex())
            {
                var rowView = (DataRowView)ResourceGrid.SelectedItem;
                if (rowView != null)
                {
                    var dlg = new OpenFileDialog();
                    dlg.Filter = "Image Files|*.png;*.jpg;*.jpeg;*.bmp;*.gif";
                    if (dlg.ShowDialog() == true)
                    {
                        rowView["Image"] = dlg.FileName;
                    }
                }
            }
        }

        private int GetImageColumnIndex()
        {
            for (int i = 0; i < _table.Columns.Count; i++)
            {
                if (_table.Columns[i].ColumnName == "Image")
                    return i;
            }
            return -1;
        }

        private void SaveButton_Click(object sender, RoutedEventArgs e)
        {
            var dlg = new SaveFileDialog();
            dlg.Filter = "XML|*.xml";
            if (dlg.ShowDialog() == true)
            {
                _table.WriteXml(dlg.FileName, XmlWriteMode.WriteSchema);
            }
        }

        private void LoadButton_Click(object sender, RoutedEventArgs e)
        {
            var dlg = new OpenFileDialog();
            dlg.Filter = "XML|*.xml";
            if (dlg.ShowDialog() == true)
            {
                _table.Clear();
                _table.ReadXml(dlg.FileName);
                BuildGridColumns();
                ResourceGrid.ItemsSource = _table.DefaultView;
            }
        }
    }
}
