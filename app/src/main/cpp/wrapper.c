#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <android/log.h> 
#include "libmp3lame/lame.h"

#define LOG_TAG "LAME"
#define LOGD(format, args...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, format, ##args);
#define BUFFER_SIZE 8192
#define be_short(s) ((short) ((unsigned short) (s) << 8) | ((unsigned short) (s) >> 8))

lame_t lame;

int read_samples(FILE *input_file, short *input) {
	int nb_read;
	nb_read = fread(input, 1, sizeof(short), input_file) / sizeof(short);

	int i = 0;
	while (i < nb_read) {
		input[i] = be_short(input[i]);
		i++;
	}

	return nb_read;
}

void Java_com_example_danny_1jiang_mp3converter_utils_LameUtils_init
       (JNIEnv *env, jclass jc, jint in_num_channels, jint in_sample_rate,
       jint out_sample_rate, jint in_brate, jint in_quality){
       lame = lame_init();

       	LOGD("Init parameters:");
       	lame_set_num_channels(lame, in_num_channels);
       	LOGD("Number of channels: %d", in_num_channels);

       	lame_set_in_samplerate(lame, in_sample_rate);
       	LOGD("in sample rate: %d", in_sample_rate);

       	lame_set_out_samplerate(lame, out_sample_rate);
        LOGD("out sample rate: %d", out_sample_rate);

       	lame_set_brate(lame, in_brate);
       	LOGD("Bitrate: %d", in_brate);

       	lame_set_quality(lame, in_quality);
       	LOGD("Quality: %d", in_quality);

       	int res = lame_init_params(lame);
       	LOGD("Init returned: %d", res);
}

jint Java_com_example_danny_1jiang_mp3converter_utils_LameUtils_encode
  (JNIEnv *env, jclass jc, jshortArray buffer_left, jshortArray buffer_right,
  jint in_sample_rate, jbyteArray mp3_buffer) {

    LOGD("lame encoding started");

    //lame_encode_buffer(lame, buffer_left, buffer_right, in_sample_rate, mp3_buffer,
    //  				BUFFER_SIZE);

    jshort* j_buffer_l = (*env)->GetShortArrayElements(env, buffer_left, NULL);

    jshort* j_buffer_r = (*env)->GetShortArrayElements(env, buffer_right, NULL);

    const jsize mp3buf_size = (*env)->GetArrayLength(env, mp3_buffer);
    jbyte* j_mp3buf = (*env)->GetByteArrayElements(env, mp3_buffer, NULL);

    int result = lame_encode_buffer(lame, j_buffer_l, j_buffer_r,
                                    in_sample_rate, j_mp3buf, mp3buf_size);

    (*env)->ReleaseShortArrayElements(env, buffer_left, j_buffer_l, 0);
    (*env)->ReleaseShortArrayElements(env, buffer_right, j_buffer_r, 0);
    (*env)->ReleaseByteArrayElements(env, mp3_buffer, j_mp3buf, 0);

    return result;
}

jint Java_com_example_danny_1jiang_mp3converter_utils_LameUtils_encodeFile(JNIEnv *env,
		jobject jobj, jstring in_source_path, jstring in_target_path) {
	const char *source_path, *target_path;
	source_path = (*env)->GetStringUTFChars(env, in_source_path, NULL);
	target_path = (*env)->GetStringUTFChars(env, in_target_path, NULL);

    int read, write;

    FILE *pcm = fopen(source_path, "rb");
    FILE *mp3 = fopen(target_path, "wb");

    const int PCM_SIZE = 8192;
    const int MP3_SIZE = 8192;

    //short int pcm_buffer[PCM_SIZE * 2];
    // original PCM -> Mp3
    short int pcm_buffer[PCM_SIZE];

    unsigned char mp3_buffer[MP3_SIZE];

    do {
        // original PCM -> Mp3
        //read = fread(pcm_buffer, 2 * sizeof(short int), PCM_SIZE, pcm);

        read = fread(pcm_buffer, sizeof(short int), PCM_SIZE, pcm);
        LOGD("read %d already", read);
        if (read == 0)
            write = lame_encode_flush(lame, mp3_buffer, MP3_SIZE);
        else
            write = lame_encode_buffer(lame, pcm_buffer, pcm_buffer, read, mp3_buffer, MP3_SIZE);
        fwrite(mp3_buffer, write, 1, mp3);
    } while (read != 0);

    lame_close(lame);
    fclose(mp3);
    fclose(pcm);

    return 0;
}
