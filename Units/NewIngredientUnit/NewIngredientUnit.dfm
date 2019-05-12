object NewIngredientForm: TNewIngredientForm
  Left = 322
  Top = 164
  BorderStyle = bsDialog
  Caption = #1053#1086#1074#1099#1081' '#1080#1085#1075#1088#1080#1076#1080#1077#1085#1090
  ClientHeight = 236
  ClientWidth = 743
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
  object GroupBox1: TGroupBox
    Left = 0
    Top = 0
    Width = 737
    Height = 225
    TabOrder = 0
    object nameLabel: TLabel
      Left = 8
      Top = 24
      Width = 37
      Height = 19
      Caption = #1048#1084#1103': '
    end
    object Label1: TLabel
      Left = 8
      Top = 96
      Width = 49
      Height = 19
      Caption = #1041#1077#1083#1082#1080': '
    end
    object Label2: TLabel
      Left = 8
      Top = 128
      Width = 49
      Height = 19
      Caption = #1046#1080#1088#1099': '
    end
    object Label3: TLabel
      Left = 8
      Top = 160
      Width = 72
      Height = 19
      Caption = #1059#1075#1083#1077#1074#1086#1076#1099': '
    end
    object Label4: TLabel
      Left = 8
      Top = 64
      Width = 28
      Height = 19
      Caption = #1058#1080#1087':'
    end
    object definitionLabel: TLabel
      Left = 344
      Top = 24
      Width = 77
      Height = 19
      Caption = #1054#1087#1080#1089#1072#1085#1080#1077': '
    end
    object nameEdit: TEdit
      Left = 96
      Top = 24
      Width = 241
      Height = 27
      TabOrder = 0
    end
    object protEdit: TEdit
      Left = 96
      Top = 88
      Width = 241
      Height = 27
      TabOrder = 1
    end
    object fatsEdit: TEdit
      Left = 96
      Top = 120
      Width = 241
      Height = 27
      TabOrder = 2
    end
    object carbEdit: TEdit
      Left = 96
      Top = 152
      Width = 241
      Height = 27
      TabOrder = 3
    end
    object typeComboBox: TComboBox
      Left = 96
      Top = 56
      Width = 241
      Height = 27
      ItemHeight = 19
      TabOrder = 4
    end
    object saveButton: TButton
      Left = 8
      Top = 184
      Width = 721
      Height = 33
      Caption = #1057#1086#1093#1088#1072#1085#1080#1090#1100
      TabOrder = 5
      OnClick = saveButtonClick
    end
    object descriptionMemo: TMemo
      Left = 344
      Top = 56
      Width = 385
      Height = 121
      ScrollBars = ssVertical
      TabOrder = 6
    end
  end
end
