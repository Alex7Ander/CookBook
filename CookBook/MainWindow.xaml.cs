using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Collections.ObjectModel;
using System.Collections;

namespace CookBook
{
    public partial class MainWindow : Window
    {
        public PhotoWindow pWin;
        List<string> pathList;

        public MainWindow()
        {
            InitializeComponent();
            pathList = new List<string>();
            this.Loaded += MainWindow_Loaded;
        }

        private void MainWindow_Loaded(object sender, RoutedEventArgs e)
        {
            Book.openBook();
            this.fillTypesComboBox();
            TypesComboBox.SelectedIndex = 0;
            int index = TypesComboBox.SelectedIndex;
            this.fillRecipeList(index);

            photoPanel.Orientation = Orientation.Horizontal;
            photoPanel.HorizontalAlignment = HorizontalAlignment.Center;
            this.showPictures(7);
            
        }

        private void showPictures(int countOfPictures)
        {
            pathList.Clear();
            for (int i = 0; i < countOfPictures; i++)
            {
                Image img = new Image();
                img.Name = "img_" + i.ToString();
                photoPanel.Children.Add(img);
                img.Width = 130;
                img.Height = 130;
                img.Stretch = Stretch.Fill;
                string path = Environment.CurrentDirectory + "/files/Images/Init/" + (i + 1).ToString() + ".jpg";
                ImageSource image = new BitmapImage(new Uri(Environment.CurrentDirectory + "/files/Images/Init/" + (i + 1).ToString() + ".jpg", UriKind.Absolute));
                img.Source = image;
                pathList.Add(path);
            }
        }

        public int fillTypesComboBox()
        {
            TypesComboBox.Items.Clear();
            try
            {
                foreach (string str in Book.ListOfTypes)
                {
                    TextBlock tBlock = new TextBlock();
                    tBlock.Text = str;
                    tBlock.FontWeight = FontWeights.Bold;
                    tBlock.TextDecorations = TextDecorations.Underline;
                    TypesComboBox.Items.Add(tBlock);
                }
                return 0;
            }
            catch (Exception exp)
            {
                return -1;
            }
        }

        public int fillRecipeList(int indexOfType)
        {
            RecipeList.Items.Clear();
            int step = 1;
            try
            {
                foreach (string str in Book.ListOfRecipes[indexOfType])
                {
                    ListBoxItem item = new ListBoxItem();
                    if (step % 2 != 0)
                        item.Background = Brushes.WhiteSmoke;
                    else
                        item.Background = Brushes.LightGray;
                    item.Content = str;
                    this.RecipeList.Items.Add(item);
                    step++;
                }
                return 0;
            }
            catch (Exception exp)
            {
                return -1;
            }
        }

        private void Controll_Recipes_Btn_Click(object sender, RoutedEventArgs e)
        {
            AddRecipeWindow newRecipeWindow = new AddRecipeWindow();
            newRecipeWindow.Show();
            newRecipeWindow.Owner = this;
        }

        private void TempBtn_Click(object sender, RoutedEventArgs e)
        {

        }

        private void TypesComboBox_SelectionChanged(object sender, RoutedEventArgs e)
        {
            int index = TypesComboBox.SelectedIndex;
            this.fillRecipeList(index);
        }

        private void aPicture_MouseDown(object sender, MouseButtonEventArgs e)
        {
            if (pWin == null)
            {
                pWin = new PhotoWindow();
                pWin.Owner = this;
                pWin.Show();
            }
            else
            {
                //
            }
            return;
        }
    }
}
