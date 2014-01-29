using System;
using Android.Hardware;
using System.Collections.Generic;

namespace MotionSensorDemo
{
	public class SensorValueStruct
	{
		private SensorType type;
		private long timestamp;
		private IList<float> values;
		private SensorStatus accuracy;

		public SensorValueStruct()
		{
		}
		public SensorValueStruct(SensorType type, long timestamp, IList<float> values)
		{
			SensorValueStruct(type, timestamp, values, SensorStatus.AccuracyHigh);
		}

		public SensorValueStruct(SensorType type, long timestamp, IList<float> values, SensorStatus accuracy)
		{
			this.type = type;
			this.timestamp = timestamp;
			this.values = values;
			this.accuracy = accuracy;
		}

		public SensorType GetType() {
			return type;
		}
		public void SetType(SensorType type) {
			this.type = type;
		}

		public long GetTimestamp() {
			return timestamp;
		}
		public void SetTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public IList<float> GetValues() {
			return values;
		}
		public void SetValues(float[] values) {
			this.values = values;
		}

		public SensorStatus GetAccuracy() {
			return accuracy;
		}
		public void SetAccuracy(SensorStatus accuracy) {
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

