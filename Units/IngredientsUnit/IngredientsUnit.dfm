object IngredientsForm: TIngredientsForm
  Left = 369
  Top = 137
  BorderStyle = bsDialog
  Caption = #1048#1085#1075#1088#1077#1076#1080#1077#1085#1090#1099
  ClientHeight = 442
  ClientWidth = 662
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
  object TypeGroupBox: TGroupBox
    Left = 0
    Top = 0
    Width = 313
    Height = 65
    Caption = #1058#1080#1087' '
    TabOrder = 0
    object TypeComboBox: TComboBox
      Left = 8
      Top = 24
      Width = 297
      Height = 27
      Style = csDropDownList
      ItemHeight = 19
      TabOrder = 0
      OnChange = TypeComboBoxChange
    end
  end
  object IngredientsGroupBox: TGroupBox
    Left = 0
    Top = 72
    Width = 313
    Height = 369
    Caption = #1048#1085#1075#1088#1077#1076#1080#1077#1085#1090#1099' '
    TabOrder = 1
    object IngredientsListBox: TListBox
      Left = 8
      Top = 24
      Width = 297
      Height = 337
      ItemHeight = 19
      TabOrder = 0
      OnClick = IngredientsListBoxClick
    end
  end
  object GroupBox1: TGroupBox
    Left = 320
    Top = 0
    Width = 337
    Height = 441
    Caption = #1048#1085#1092#1086#1088#1084#1072#1094#1080#1103': '
    TabOrder = 2
    object Label1: TLabel
      Left = 8
      Top = 24
      Width = 224
      Height = 19
      Caption = #1053#1072' 100 '#1075#1088'. '#1087#1088#1086#1076#1091#1082#1090#1072' '#1087#1088#1080#1093#1086#1076#1080#1090#1089#1103': '
    end
    object Label2: TLabel
      Left = 8
      Top = 48
      Width = 71
      Height = 19
      Caption = #1041#1077#1083#1082#1080', '#1075#1088'.:'
    end
    object Label3: TLabel
      Left = 8
      Top = 88
      Width = 75
      Height = 19
      Caption = #1046#1080#1088#1099', '#1075#1088'.: '
    end
    object Label4: TLabel
      Left = 8
      Top = 120
      Width = 94
      Height = 19
      Caption = #1059#1075#1083#1077#1074#1086#1076#1099', '#1075#1088'.:'
    end
    object Label5: TLabel
      Left = 8
      Top = 152
      Width = 159
      Height = 19
      Caption = #1069#1085#1077#1088#1075'. '#1094#1077#1085#1085#1086#1089#1090#1100' ('#1082#1082#1072#1083'):'
    end
    object Label6: TLabel
      Left = 8
      Top = 184
      Width = 77
      Height = 19
      Caption = #1054#1087#1080#1089#1072#1085#1080#1077': '
    end
    object DeleteButton: TButton
      Left = 8
      Top = 376
      Width = 321
      Height = 25
      Caption = #1059#1076#1072#1083#1080#1090#1100
      TabOrder = 0
      OnClick = DeleteButtonClick
    end
    object EditButton: TButton
      Left = 8
      Top = 344
      Width = 321
      Height = 25
      Caption = #1057#1086#1093#1088#1072#1085#1080#1090#1100' '#1080#1079#1084#1077#1085#1077#1085#1080#1103
      TabOrder = 1
      OnClick = EditButtonClick
    end
    object AddNewButton: TButton
      Left = 8
      Top = 408
      Width = 321
      Height = 25
      Caption = #1044#1086#1073#1072#1074#1080#1090#1100' '#1085#1086#1074#1099#1081
      TabOrder = 2
      OnClick = AddNewButtonClick
    end
    object energyEdit: TEdit
      Left = 168
      Top = 144
      Width = 161
      Height = 27
      TabOrder = 3
    end
    object protEdit: TEdit
      Left = 168
      Top = 48
      Width = 161
      Height = 27
      TabOrder = 4
    end
    object fatsEdit: TEdit
      Left = 168
      Top = 80
      Width = 161
      Height = 27
      TabOrder = 5
    end
    object carbEdit: TEdit
      Left = 168
      Top = 112
      Width = 161
      Height = 27
      TabOrder = 6
    end
    object descriptionMemo: TMemo
      Left = 8
      Top = 208
      Width = 321
      Height = 129
      Font.Charset = RUSSIAN_CHARSET
      Font.Color = clWindowText
      Font.Height = -13
      Font.Name = 'Calibri'
      Font.Style = []
      ParentFont = False
      ScrollBars = ssVertical
      TabOrder = 7
    end
  end
end
