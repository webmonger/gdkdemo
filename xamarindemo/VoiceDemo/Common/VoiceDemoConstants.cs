
namespace VoiceDemo.Common
{
// temporary
	public sealed class VoiceDemoConstants
	{
	    // The name of the "extra" key for VoiceDemo intent service.
		public static string EXTRA_KEY_VOICE_ACTION = "extra_action";

	    // Action commands. (One word)
		public static string ACTION_START_DICTATION = "dictate";
		public static string ACTION_STOP_VOICEDEMO = "stop";
	    // ...

	    // For onActivityResult()
	    public static int SPEECH_REQUEST = 0;
	    // ...

	}
}