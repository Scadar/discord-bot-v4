package com.example.discordbotv4.listeners;

import com.example.discordbotv4.services.LavaplayerAudioSource;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.stereotype.Component;

@Component
public class VoiceConnect implements MessageCreateListener {

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageContent().startsWith("схс")) {
            if (event.getServer().isPresent() && event.getMessageAuthor().asUser().isPresent()) {


                Server server = event.getServer().get();
                ServerVoiceChannel voiceChannel = event.getMessageAuthor().asUser().get().getConnectedVoiceChannel(server).get();

                voiceChannel.connect().thenAccept(audioConnection -> {
                    long track = 0;
                    try {
                        track = Long.parseLong(event.getMessageContent().split(" ")[1]);
                    } catch (Exception ignored){}

                    String trackUrl = "";

                    if (track == 1) {
                        trackUrl = "https://www.youtube.com/watch?v=lgrkNyeG3a0";
                    } else {
                        trackUrl = "https://www.youtube.com/watch?v=DsX0opUvKws";
                    }
                    AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
                    playerManager.registerSourceManager(new YoutubeAudioSourceManager());
                    AudioPlayer player = playerManager.createPlayer();

                    playerManager.loadItem(trackUrl, new AudioLoadResultHandler() {
                        @Override
                        public void trackLoaded(AudioTrack track) {
                            player.playTrack(track);
                        }

                        @Override
                        public void playlistLoaded(AudioPlaylist playlist) {
                            for (AudioTrack track : playlist.getTracks()) {
                                player.playTrack(track);
                            }
                        }

                        @Override
                        public void noMatches() {

                        }

                        @Override
                        public void loadFailed(FriendlyException throwable) {
                            // Notify the user that everything exploded
                        }

                    });

                    AudioEventListener audioEventListener = audioEvent -> {
                        if (audioEvent.player.getPlayingTrack() == null) {
                            System.out.println("!111111!!");
                            audioConnection.close();
                        }
                    };

                    AudioSource source = new LavaplayerAudioSource(event.getApi(), player, audioEventListener);

                    audioConnection.setAudioSource(source);

                }).exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });

            }
        }
    }
}
