package ru.pavlov.yandex.disk;

public class YandexDiskInternalException extends YandexDiskException{
	private static final long serialVersionUID = 1L;

	YandexDiskInternalException(String code) {
		super("������ �� ������� �������. ��� ������: " + code);
	}

}
