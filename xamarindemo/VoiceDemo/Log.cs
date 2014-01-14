using System;
using SystemClock = Android.OS.SystemClock;
using Java.Lang;
using VoiceDemo.Common;

namespace VoiceDemo
{
	internal static class Log 
	{
		private const string TAG = "VoiceDemo";

		internal static readonly bool V = Config.LogLevel <= Android.Util.Log.Verbose;
		internal static readonly bool D = Config.LogLevel <= Android.Util.Log.Debug;
		internal static readonly bool I = Config.LogLevel <= Android.Util.Log.Info;
		internal static readonly bool W = Config.LogLevel <= Android.Util.Log.Warn;
		internal static readonly bool E = Config.LogLevel <= Android.Util.Log.Error;

		static void v(string logMe)
		{
			Android.Util.Log.v(TAG, SystemClock.UptimeMillis() + " " + logMe);
		}
		static void v(string logMe, Throwable ex)
		{
			Android.Util.Log.v(TAG, SystemClock.UptimeMillis() + " " + logMe, ex);
		}

		static void d(string logMe) 
		{
			Android.Util.Log.d(TAG, SystemClock.UptimeMillis() + " " + logMe);
		}
		static void d(string logMe, Throwable ex)
		{
			Android.Util.Log.d(TAG, SystemClock.UptimeMillis() + " " + logMe, ex);
		}

		static void i(string logMe)
		{
			Android.Util.Log.i(TAG, logMe);
		}
		static void i(string logMe, Throwable ex)
		{
			Android.Util.Log.i(TAG, logMe, ex);
		}

		static void w(string logMe)
		{
			Android.Util.Log.w(TAG, logMe);
		}
		static void w(string logMe, Throwable ex)
		{
			Android.Util.Log.w(TAG, logMe, ex);
		}

		static void e(string logMe)
		{
			Android.Util.Log.e(TAG, logMe);
		}
		static void e(string logMe, System.Exception ex)
		{
			Android.Util.Log.e(TAG, logMe, ex);
		}
	}
}
