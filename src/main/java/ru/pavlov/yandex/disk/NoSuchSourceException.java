package ru.pavlov.yandex.disk;

public class NoSuchSourceException extends YandexDiskException {
	private static final long serialVersionUID = 1L;

	NoSuchSourceException() {
		super("������ �� ���������� url ����������� ��� url �� ���������");
	}

}
