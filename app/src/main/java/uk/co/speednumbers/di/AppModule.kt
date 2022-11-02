package uk.co.explorer.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.co.speednumbers.R
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPrefs(app: Application): SharedPreferences = app.getSharedPreferences(
        "PREFS",
        Context.MODE_PRIVATE
    )!!

    @Provides
    @Singleton
    fun provideMediaPlayerSound(): SoundPool = SoundPool.Builder()
        .setMaxStreams(2)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build();

    @Provides
    @Singleton
    fun provideSoundId(soundPool: SoundPool, app: Application): Int =
        soundPool.load(app, R.raw.correct_ding, 1)

}