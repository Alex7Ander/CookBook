object NewRecieptForm: TNewRecieptForm
  Left = 235
  Top = 127
  BorderStyle = bsDialog
  Caption = #1053#1086#1074#1099#1081' '#1088#1077#1094#1077#1087#1090
  ClientHeight = 485
  ClientWidth = 912
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
  object MainInfoGroupBox: TGroupBox
    Left = 0
    Top = 0
    Width = 369
    Height = 105
    Caption = #1048#1084#1103' '#1080' '#1090#1080#1087' '
    TabOrder = 0
    object NameLabel: TLabel
      Left = 8
      Top = 32
      Width = 37
      Height = 19
      Caption = #1048#1084#1103': '
    end
    object TypeLabel: TLabel
      Left = 8
      Top = 72
      Width = 28
      Height = 19
      Caption = #1058#1080#1087':'
    end
    object NameEdit: TEdit
      Left = 48
      Top = 24
      Width = 313
      Height = 27
      TabOrder = 0
    end
    object TypeComboBox: TComboBox
      Left = 48
      Top = 64
      Width = 313
      Height = 27
      ItemHeight = 19
      TabOrder = 1
    end
  end
  object GroupBox1: TGroupBox
    Left = 0
    Top = 112
    Width = 369
    Height = 321
    Caption = #1054#1087#1080#1089#1072#1085#1080#1077' '
    TabOrder = 1
    object DescriptionMemo: TMemo
      Left = 8
      Top = 24
      Width = 353
      Height = 289
      ScrollBars = ssVertical
      TabOrder = 0
    end
  end
  object SaveButton: TButton
    Left = 0
    Top = 440
    Width = 905
    Height = 33
    Caption = #1057#1086#1093#1088#1072#1085#1080#1090#1100
    TabOrder = 2
    OnClick = SaveButtonClick
  end
  object IngredientsGroupBox: TGroupBox
    Left = 376
    Top = 0
    Width = 529
    Height = 433
    Caption = #1048#1085#1075#1088#1080#1076#1080#1077#1085#1090#1099
    TabOrder = 3
    object Label1: TLabel
      Left = 248
      Top = 24
      Width = 176
      Height = 19
      Caption = #1048#1085#1075#1088#1077#1076#1080#1077#1085#1090#1099' '#1076#1083#1103' '#1073#1083#1102#1076#1072':'
    end
    object IngredientsTypeComboBox: TComboBox
      Left = 8
      Top = 24
      Width = 225
      Height = 27
      ItemHeight = 19
      TabOrder = 0
      OnChange = IngredientsTypeComboBoxChange
    end
    object IngredientsListBox: TListBox
      Left = 8
      Top = 56
      Width = 225
      Height = 369
      ItemHeight = 19
      TabOrder = 1
      OnDblClick = IngredientsListBoxDblClick
    end
    object DeleteButton: TButton
      Left = 248
      Top = 48
      Width = 273
      Height = 25
      Caption = #1059#1076#1072#1083#1080#1090#1100
      TabOrder = 2
      OnClick = DeleteButtonClick
    end
    object NecessaryIngredientsListEditor: TValueListEditor
      Left = 248
      Top = 80
      Width = 273
      Height = 345
      TabOrder = 3
      TitleCaptions.Strings = (
        #1048#1085#1075#1088#1077#1076#1080#1077#1085#1090
        #1050#1086#1083#1080#1095#1077#1089#1090#1074#1086)
      ColWidths = (
        150
        117)
    end
  end
end
