using System;
using Android.Hardware;

namespace MotionSensorDemo
{
	public class SensorValueStruct
	{
		private int type;
		private long timestamp;
		private float[] values;
		private int accuracy;

		public SensorValueStruct()
		{
		}
		public SensorValueStruct(int type, long timestamp, float[] values)
		{
			this(type, timestamp, values, SensorDelay.Normal);
		}

		public SensorValueStruct(int type, long timestamp, float[] values, int accuracy)
		{
			this.type = type;
			this.timestamp = timestamp;
			this.values = values;
			this.accuracy = accuracy;
		}

		public int GetType() {
			return type;
		}
		public void SetType(int type) {
			this.type = type;
		}

		public long GetTimestamp() {
			return timestamp;
		}
		public void SetTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public float[] GetValues() {
			return values;
		}
		public void SetValues(float[] values) {
			this.values = values;
		}

		public int GetAccuracy() {
			return accuracy;
		}
		public void SetAccuracy(int accuracy) {
			this.accuracy = accuracy;
		}

		public override string ToString() {
			return string.Join(",", values);
//        return "SensorValueStruct{" +
//                "type=" + type +
//                ", timestamp=" + timestamp +
//                ", values=" + Arrays.tostring(values) +
//                ", accuracy=" + accuracy +
//                '}';
		}
	}
}

