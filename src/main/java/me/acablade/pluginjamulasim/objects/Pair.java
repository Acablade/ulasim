package me.acablade.pluginjamulasim.objects;

public class Pair<K, V> {
	private K key;
	private V value;

	public Pair() {
	}

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return this.key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return this.value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public int hashCode() {
		int result = 1;
		result = 31 * result + (this.key == null ? 0 : this.key.hashCode());
		result = 31 * result + (this.value == null ? 0 : this.value.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (this.getClass() != obj.getClass()) {
			return false;
		} else {
			Pair<?, ?> other = (Pair)obj;
			if (this.key == null) {
				if (other.key != null) {
					return false;
				}
			} else if (!this.key.equals(other.key)) {
				return false;
			}

			if (this.value == null) {
				if (other.value != null) {
					return false;
				}
			} else if (!this.value.equals(other.value)) {
				return false;
			}

			return true;
		}
	}

	public String toString() {
		return "{k=" + this.key + ",v=" + this.value + "}";
	}
}
