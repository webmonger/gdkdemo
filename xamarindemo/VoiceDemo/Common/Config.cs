

namespace VoiceDemo.Common
{
	public sealed class Config
	{
		private Config()
		{
		}

		// Log level.
		public static int LogLevel = Android.Util.Log.Verbose();
		//static final int LOGLEVEL = Android.Util.Log.Debug;
		//static final int LOGLEVEL = Android.Util.Log.Info;

	}
}
