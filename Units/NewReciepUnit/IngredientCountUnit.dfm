object IngredientCountForm: TIngredientCountForm
  Left = 510
  Top = 235
  BorderIcons = []
  BorderStyle = bsDialog
  ClientHeight = 89
  ClientWidth = 423
  Color = clBtnFace
  Font.Charset = RUSSIAN_CHARSET
  Font.Color = clWindowText
  Font.Height = -16
  Font.Name = 'Calibri'
  Font.Style = []
  OldCreateOrder = False
  PixelsPerInch = 96
  TextHeight = 19
  object CountGroupBox: TGroupBox
    Left = 0
    Top = 0
    Width = 417
    Height = 81
    Caption = #1050#1086#1083#1080#1095#1077#1089#1090#1074#1086' '#1080#1085#1075#1088#1077#1076#1080#1077#1085#1090#1072' ('#1075#1088'/'#1084#1083')'
    TabOrder = 0
    object CountEdit: TEdit
      Left = 8
      Top = 32
      Width = 401
      Height = 27
      TabOrder = 0
      OnKeyDown = CountEditKeyDown
    end
  end
end
