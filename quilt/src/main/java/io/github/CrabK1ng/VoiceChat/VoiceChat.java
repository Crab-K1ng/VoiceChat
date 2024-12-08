package io.github.CrabK1ng.VoiceChat;

import com.badlogic.gdx.utils.PauseableThread;
import dev.crmodders.cosmicquilt.api.entrypoint.ModInitializer;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.networking.GamePacket;
//import finalforeach.cosmicreach.networking.client.ClientNetworkManager;
import io.github.CrabK1ng.VoiceChat.networking.VoiceChatPacket;
import org.quiltmc.loader.api.ModContainer;

import javax.sound.sampled.*;

import static io.github.CrabK1ng.VoiceChat.Constants.LOGGER;

public class VoiceChat implements ModInitializer {
	public static SourceDataLine speakers;
	public static TargetDataLine microphone;

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Example Mod Initialized!");
		GamePacket.registerPacket(VoiceChatPacket.class);
	}

	public static void captureAudio() {
		PauseableThread AudioCapture = Threads.createPauseableThread("AudioCaptureThread", () -> {
			try {
				byte[] buffer = new byte[1024];
				while (true) {
					int bytesRead = microphone.read(buffer, 0, buffer.length);
					if (bytesRead > 0) {
						VoiceChatPacket VoiceChatPacket = new VoiceChatPacket(buffer.clone());
						//ClientNetworkManager.sendAsClient(VoiceChatPacket);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		AudioCapture.start();
	}

	public static void openMicrophone(){
		try {
			AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			if (!AudioSystem.isLineSupported(info)) {
				throw new LineUnavailableException("Line not supported");
			}

			microphone = (TargetDataLine) AudioSystem.getLine(info);
			microphone.open(format);
			microphone.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void openSpeaker(){
		try {
			AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

			if (!AudioSystem.isLineSupported(info)) {
				throw new LineUnavailableException("Line not supported");
			}

			speakers = (SourceDataLine) AudioSystem.getLine(info);
			speakers.open(format);
			speakers.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

