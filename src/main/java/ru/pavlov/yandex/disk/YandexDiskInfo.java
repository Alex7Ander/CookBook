package ru.pavlov.yandex.disk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YandexDiskInfo {
	private String unlimited_autoupload_enabled;
	private String  max_file_size;
	private String  total_space;
	private String  trash_size;
	private String  used_space;
	public String getUnlimited_autoupload_enabled() {
		return unlimited_autoupload_enabled;
	}
	public void setUnlimited_autoupload_enabled(String unlimited_autoupload_enabled) {
		this.unlimited_autoupload_enabled = unlimited_autoupload_enabled;
	}
	public String getMax_file_size() {
		return max_file_size;
	}
	public void setMax_file_size(String max_file_size) {
		this.max_file_size = max_file_size;
	}
	public String getTotal_space() {
		return total_space;
	}
	public void setTotal_space(String total_space) {
		this.total_space = total_space;
	}
	public String getTrash_size() {
		return trash_size;
	}
	public void setTrash_size(String trash_size) {
		this.trash_size = trash_size;
	}
	public String getUsed_space() {
		return used_space;
	}
	public void setUsed_space(String used_space) {
		this.used_space = used_space;
	}
	
	@Override
	public String toString() {
		return  "unlimited_autoupload_enabled: " + unlimited_autoupload_enabled + 
				"\nmax_file_size: " + max_file_size + 
				"\ntotal_space: " + total_space +
				"\ntrash_size: " + trash_size +
				"\nused_space: " + used_space;
	}
}
