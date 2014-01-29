using System;
using System.Collections.Generic;
using Android.Hardware;

namespace EnvironmentalSensorDemo
{
	public class SensorValueStruct
	{
        private SensorType _type;
		private long timestamp;
		private IList<float> values;
        private SensorStatus accuracy;

		public SensorValueStruct()
		{
		}
        public SensorValueStruct(SensorType type, long timestamp, float[] values)
            : this(type, timestamp, values, SensorStatus.AccuracyMedium)
		{
		}

        public SensorValueStruct(SensorType type, long timestamp, IList<float> values, SensorStatus accuracy)
		{
			this._type = type;
			this.timestamp = timestamp;
			this.values = values;
			this.accuracy = accuracy;
		}

	    public SensorType Type
	    {
            get { return _type; }
	        private set { _type = value; }
	    }

        public void SetType(SensorType type)
        {
			this._type = type;
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

        public SensorStatus GetAccuracy()
        {
			return accuracy;
		}
        public void SetAccuracy(SensorStatus accuracy)
        {
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

