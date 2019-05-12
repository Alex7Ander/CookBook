object ShopListForm: TShopListForm
  Left = 248
  Top = 137
  BorderStyle = bsDialog
  Caption = #1057#1087#1080#1089#1086#1082' '#1087#1086#1082#1091#1087#1086#1082
  ClientHeight = 442
  ClientWidth = 903
  Color = clBtnFace
  Font.Charset = RUSSIAN_CHARSET
  Font.Color = clWindowText
  Font.Height = -16
  Font.Name = 'Calibri'
  Font.Style = []
  OldCreateOrder = False
  OnClose = FormClose
  OnShow = FormShow
  PixelsPerInch = 96
  TextHeight = 19
  object RecipesListGroupBox: TGroupBox
    Left = 0
    Top = 0
    Width = 321
    Height = 441
    Caption = #1042#1099#1073#1077#1088#1080#1090#1077' '#1073#1083#1102#1076#1072', '#1095#1090#1086' '#1078#1077#1083#1072#1077#1090#1077' '#1087#1088#1080#1075#1086#1090#1086#1074#1080#1090#1100': '
    TabOrder = 0
    object TypesComboBox: TComboBox
      Left = 8
      Top = 24
      Width = 305
      Height = 27
      ItemHeight = 19
      TabOrder = 0
      OnChange = TypesComboBoxChange
    end
    object RecipesListBox: TListBox
      Left = 8
      Top = 56
      Width = 305
      Height = 377
      ItemHeight = 19
      TabOrder = 1
      OnDblClick = RecipesListBoxDblClick
    end
  end
  object CheckedRecipesGroupBox: TGroupBox
    Left = 328
    Top = 0
    Width = 265
    Height = 441
    Caption = #1042#1072#1096' '#1089#1087#1080#1089#1086#1082' '#1073#1083#1102#1076' '
    TabOrder = 1
    object checkedRecipesListBox: TListBox
      Left = 8
      Top = 24
      Width = 249
      Height = 409
      ItemHeight = 19
      TabOrder = 0
    end
  end
  object GroupBox1: TGroupBox
    Left = 600
    Top = 0
    Width = 297
    Height = 441
    Caption = #1057#1087#1080#1089#1086#1082' '#1087#1086#1082#1091#1087#1086#1082' '
    TabOrder = 2
    object ShopListBox: TListBox
      Left = 8
      Top = 24
      Width = 281
      Height = 369
      ItemHeight = 19
      TabOrder = 0
    end
    object SaveButton: TButton
      Left = 8
      Top = 400
      Width = 281
      Height = 33
      Caption = #1057#1086#1093#1088#1072#1085#1080#1090#1100
      TabOrder = 1
      OnClick = SaveButtonClick
    end
  end
end
