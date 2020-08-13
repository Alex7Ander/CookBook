package ru.pavlov.yandex.disk;

public class NoConnectionToYandexDiskException extends YandexDiskException{
	private static final long serialVersionUID = 1L;

	NoConnectionToYandexDiskException() {
		super("��� ���������� � ������ ������");
	}

}
