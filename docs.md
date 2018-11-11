# Android Agora SDK API 应用范例
## 开发环境配置注意事项
* 升级Android Studio以及设置合适的gradle版本（注意区分gradle和gradle插件的版本）
* 若应用里划分了多个模块，则需要注意implementation、compile以及compileOnly的区别
* 若下载插件或依赖包时出现无法连接等问题，尝试运行 ***Android Studio -> File -> Invalidate caches / Restart***

## 检验应用权限

Agora音视频SDK需要获取某些应用权限才能正常工作，权限列表请查看[添加权限](https://docs.agora.io/cn/Video/android_video?platform=Android#%E6%B7%BB%E5%8A%A0%E6%9D%83%E9%99%90)

* 若应用需要操作蓝牙耳机等设备，则需要额外注册蓝牙权限
	``` xml
    <uses-permission android:name="android.permission.BLUETOOTH" />
	```

* 在Android 6.0+版本上必须动态申请权限（也叫运行时权限申请，弹出窗口由用户确认赋予权限）。并不是所有配置过的权限都需要动态申请的。以下3个权限必须经用户确认通过：
	- android.permission.CAMERA
	- android.permission.WRITE_EXTERNAL_STORAGE
	- android.permission.RECORD_AUDIO

	权限可以选择在某个Launching Activity进行验证，也可以为所有的Activity写一个BaseActivity，总之保证在应用运行的时候必须经过权限检查。

### 实现方法

``` Java
	// 检查权限是否已经通过
	int passed = PackageManager.PERMISSION_GRANTED;
	boolean granted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == passed &&
		ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == passed &&
		ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == passed);

	// 若发现并不是所有权限都通过，则发起动态获取权限的请求
	if (!granted) {
		ActivityCompat.requestPermissions(activity,
			new String[] { Manifest.permission.CAMERA,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.RECORD_AUDIO },
			REQUEST_CODE_ALL_PERMISSIONS);
	}

	// 位于所在Activity的系统回调里检查申请结果，可以在确认所有权限均申请成功之后进行Agora RTC的初始化
	@Override
		public void onRequestPermissionsResult(int requestCode, 
					@NonNull String[] permissions, @NonNull int[] grantResults) {
			for (int result : grantResults) {
				if (result != PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, getString("Permission has been denied."),
							Toast.LENGTH_SHORT).show();
					return;
				}
			}

			Toast.makeText(this, getString("All permissions have been granted"),
					Toast.LENGTH_SHORT).show();
		}
``` 

## 通话前的网络质量检测
### 实现方法

注：所有示例代码均假设rtcEngine已经初始化完毕。

``` Java
	// 在合适的时机启动lastmile测试，启用网络测试的情景请参考方法文档
	rtcEngine.enableLastmileTest();

	// 位于全局IRtcEngineEventHandler中会发生回调
	public void onLastmileQuality(int quality) {
		// 若启动了lastmile测试，在还未获得此回调之前不要调用其它方法
 		// quality即为当前检测到的quality类型，可以
		// 根据此参数执行相关逻辑。
		// ⑴ 可以选择在回调内部结束测试
		rtcEngine.disableLastmileTest();
	}

	// ⑵ 也可以选择其它时候结束测试, 结束测试之前
	// onLastmileQuality() 回调可能会被调用多次
	rtcEngine.disableLastmileTest();
```

### API参考
 * [RtcEngine.enableLastmileTest()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a35d045b585649ca89377ed82e9cf0662)
 * [RtcEngine.disableLastmileTest()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a35d045b585649ca89377ed82e9cf0662)
 * [IRtcEngineEventHandler.onLastmileQuality()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_i_rtc_engine_event_handler.html#a2887941e3c105c21309bd2643372e7f5)

### 开发注意事项
* 所有相关的API都有返回值。返回值小于0表示API调用失败

## 语音通话使能、音量相关
```Java
	// 开启/关闭audio模块，频道内外均有效。audio模块默认开启。
	// 方法重置audio整个模块，因此响应速度较慢。
	rtcEngine.enableAudio();
	rtcEngine.disableAudio();

	// 若仅仅涉及到禁用语音的用途，则推荐用以下几个方法灵活配置

	// 开启/禁止本地麦克风采集和创建本地音频流
	// 此方法会停掉采集，且关闭音频流
	rtcEngine.enableLocalAudio(true);
	rtcEngine.enableLocalAudio(false);

	// 停止接受所有远端音频流
	rtcEngine.muteAllRemoteAudioStreams(true);

	// 静音本地音频流，含义是本地采集过程和音频
	// 流仍然存在，只不过音频数据不发送出去
	rtcEngine.muteLocalAudioStream(true);

	// 停止接受指定远端的音频流，uid表示对方的识别号
	rtcEngine.muteRemoteAudioStream(uid, false);

	// 默认接受所有的远端音频流.该方法在加入频道前后都可调用。
	// 如果在加入频道后调用，会接收不到后面加入频道的用户的音频流
	rtcEngine.setDefaultMuteAllRemoteAudioStreams(true);

	// 调节音量，参数值范围是 0 ~ 400，100表示原始音量，400表示原始音量的4倍
	int volume = 200;
	// 设置录音信号音量
	rtcEngine.adjustRecordingSignalVolume(volume);
	// 设置播放信号音量
	rtcEngine.adjustPlaybackSignalVolume(volume);
```
### API参考
* [enableAudio()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a99ae52334d3fa255dfcb384b78b91c52)
* [disableAudio()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a8d6fad1572e62c553a660a70663c682f)
* [enableLocalAudio()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a06cf8c022aceb38f34596e9def2107ad)
* [muteLocalAudioStream()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a949cd7044eec55ffd0b63ad3004db756)
* [muteRemoteAudioStream()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a4bb8c03a82b8aff52e38552e197dc8b3)
* [muteAllRemoteAudioStreams()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a1364cc35798b66094ff3799225911fda)
* [setDefaultMuteAllRemoteAudioStreams()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#abe53fbb53a4c03e9466a9c2bb53bab9f)
* [adjustRecordingSignalVolume()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af3747f72256eb683feadbca2b742bd05)
* [adjustPlaybackSignalVolume()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af7d7f10fc96db2febb9c2590891d071b)

### 开发注意事项
* 所有相关的API都有返回值。返回值小于0表示API调用失败

## 音乐混音

播放本地或者在线音乐文件，同时让同一频道的其他人听到此音乐。有如下选项：

* 混音或替换： 混音指的是音乐文件的音频流跟麦克风采集的音频流进行混合输出；替换指的是麦克风采集的音频被音乐文件的音频流替换掉，对方只能听见音乐播放。

* 循环：可以设置是否循环，以及循环次数。

### 实现方法
``` Java
	// loopCount为-1代表永久循环；其它>0的整数表示预设混音播放的循环次数
	// 若设置为不循环，则循环次数无意义
	boolean loopCount = -1;
	boolean shouldLoop = true;
	boolean replaceMic = false;

	// 开始播放混音
	rtcEngine.startAudioMixing("path/to/music", shouldLoop, replaceMic, loopCount);

	// 调整混音的音量。取值为 0 ~ 100, 100代表维持原来混音的音量（默认）。
	int volume = 50;
	rtcEngine.adjustAudioMixingVolume(volume);

	// 获取当前播放的混音音乐的时长
	int duration = rtcEngine.getAudioMixingDuration();
	// duration可以用来比如设置播放进度条的最大进度等
	// seekBar.setMax(duration);

	// 获取当前混音的播放进度
	int currentPosition = rtcEngine.getAudioMixingCurrentPosition();
	// 可以设置timer定时获取播放进度，用来显示播放进度
	// seekBar.setProgress(currentPosition);

	// 若用户拖动了进度条，可以在seekBar的回调中获取progress并重设音乐当前播放的位置
	rtcEngine.setAudioMixingPosition(progress);

	// 暂停、恢复播放
	rtcEngine.pauseAudioMixing();
	rtcEngine.resumeAudioMixing();

	// 停止播放混音，麦克风采集播放恢复
	rtcEngine.stopAudioMixing()；
```
### API参考
* [startAudioMixing()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#ac56ceea1a143a4898382bce10b04df09)
* [stopAudioMixing()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#addb1cbc23b7f725eea6eedd18412854d)
* [adjustAudioMixingVolume()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a13c5737248d5a5abf6e8eb3130aba65a)
* [pauseAudioMixing()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#ab2d4fb72ec3031f59da72b55857e0da7)
* [resumeAudioMixing()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#aedad78215c21f0a6acac7f155199f3ce)
* [getAudioMixingDuration()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a8bbeb8a8b07e4e7b1a0a493f1c66998d)
* [getAudioMixingCurrentPosition()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a5119b0e6b356f867f7e13a6e1b2bb3e5)
* [setAudioMixingPosition()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a12c3dc250c86d54552c1589dfda2e002)

### 开发注意事项

* 混音必须保证系统版本4.2以上、API Level至少为16
* 在频道内调用混音，否则会有潜在问题
* 若运行在模拟器中，混音只能播放位于 /sdcard/ 下的mp3文件
* 所有相关的API都有返回值。返回值小于0表示API调用失败

## 播放音效

音效一般指代持续很短的音频。SDK提供AudioEffectManager类统一管理音效，包含一些管理音效的常用方法。 音效由音频文件路径指定，但在AudioEffectManager内部使用sound id来识别和处理音效。音效文件通常保存在assets文件下；SDK并不强制如何定义sound id，但保证每个音效有唯一的识别即可。一般的做法有自增id，音效文件名的hashCode等。

### 实现方法
``` Java
	// 首先获取全局的音效管理类
	IAudioEffectManager manager = rtcEngine.getAudioEffectManager();

	// 预加载音效（推荐），须注意音效文件的大小，并在加入频道前完成加载
	// 仅支持mp3，aac，m4a，3gp，wav格式
	// 注：开发者可能需要额外记录id与文件路径的关联关系，用来播放和停止音效
	int id = 0;
	manager.preloadEffect(id++, "path/to/effect1");

	// 可以加载多个音效
	manager.preloadEffect(id++, "path/to/effect2");

	// 播放一个音效
	manager.playEffect(
		0,                               // 要播放的音效id
		"path/to/effect1",               // 播放文件的路径
		-1,                              // 播放次数，-1代表无限循环。直到stopEffect()或者stopAllEffects()被调用
		0.0,                             // 改变音效的空间位置，0表示正前方
		100,                             // 音量，取值0 ~ 100， 100代表原始音量
		true                             // 是否令远端也能听到音效的声音
	);

	// 暂停所有音效播放
	manager.pauseAllEffects();

	// 获取音效的音量，范围为 0 ~ 100
	double volume = manager.getEffectsVolume();

	// 保证音效音量在原始音量的80以上
	volume = volume < 80 ? 80 : volume;
	manager.setEffectsVolume(volume);

	// 继续播放暂停的音效
	manager.resumeAllEffects();

	// 停止所有音效
	manager.stopAllEffects();

	// 释放预加载的音效
	manager.unloadAllEffects();
```
### API参考
* [getAudioEffectManager()](https://docs.agora.io/cn/Interactive%20Broadcast/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#afd61b8d5e923f9e03cd419dcaf23b4af)
### 开发注意事项
* 所有相关的API都有返回值。返回值小于0表示API调用失败

## 设置音质

Agora提供不同的参数，可供开发者灵活配置适合的音质属性。SDK提供setAudioProfile()方法来完成这个设定。这个方法有2个参数，分别为：
* profile 代表不同的声音参数配置，比如采样率、码率和编码模式等
* scenario 侧重于不同的使用场景，如娱乐、教学和游戏直播等。流畅度、噪声控制、音质等频道特性会根据不同的场景做出优化

下边给出集中常用的参数组合可供开发者参考

### 实现方法
``` Java
	// 游戏开黑场景
	rtcEngine.setAudioProfile(Constants.AUDIO_PROFILE_SPEECH_STANDARD, Constants.AUDIO_SCENARIO_CHATROOM_GAMING);

	// 娱乐场景
	rtcEngine.setAudioProfile(Constants.AUDIO_PROFILE_MUSIC_STANDARD, Constants.AUDIO_SCENARIO_CHATROOM_ENTERTAINMENT);

	// KTV
	rtcEngine.setAudioProfile(Constants.AUDIO_AUDIO_PROFILE_MUSIC_HIGH_QUALITY, Constants.AUDIO_SCENARIO_CHATROOM_ENTERTAINMENT);

	// FM高音质
	rtcEngine.setAudioProfile(Constants.AUDIO_PROFILE_MUSIC_HIGH_QUALITY_STEREO, Constants.AUDIO_SCENARIO_SHOWROOM);
```

API参考
* [setAudioProfile()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a34175b5e04c88d9dc6608b1f38c0275d)

### 开发注意事项
* 所有相关的API都有返回值。返回值小于0表示API调用失败

## 耳返
### 实现方法
``` Java
	// 设置开启耳返监听功能，默认为false
	rtcEngine.enableInEarMonitoring(true);

	// 设置耳返的音量，volume的取值范围为0 ~ 100，默认为100代表耳机的原始声音
	int volume = 80;
	rtcEngine.setInEarMonitoringVolume(volume);
```

### API参考
* [enableInEarMonitoring()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#aeb014fcf7ec84291b9b39621e09772ea)
* [setInEarMonitoringVolume()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af71afdf140660b10c4fb0c40029c432d)

### 开发注意事项
* 所有相关的API都有返回值。返回值小于0表示API调用失败

## 设置音调、音色
Agora提供了一系列的方法让开发者灵活的定制自己想要的声音。比如音调、均衡和混响等。 比如，可以根据以下方式把原始声音变成绿巨人霍克的声音。
### 实现方法
```Java
    // 设置音调。可以在 [0.5, 2.0] 范围内设置。取值越小，则音调越低。默认值为 1.0，表示不需要修改音调。
	double pitch = 0.5;
    rtcEngine.setLocalVoicePitch(pitch);

    // 所有参数配置
    int[] option = { -15, 3, -9, -8, -6, -4, -3, -2, -1, 1, 10, -9, 7 6, 124, 78 };

    // 设置本地语音均衡波段的中心频率
    // 第1个参数为band frequency，取值范围[0-9]，分别代表10个区间的中心频率[31，62，125，250，500，1k,2k,4k,8k,16k] Hz
    // 第2个参数为每个频率区间的增益值，取值范围[-15,15]，单位dB, 默认值为0
    rtcEngine.setLocalVoiceEqualization(0, option[0]);
    rtcEngine.setLocalVoiceEqualization(1, option[1]);
    rtcEngine.setLocalVoiceEqualization(2, option[2]);
    rtcEngine.setLocalVoiceEqualization(3, option[3]);
    rtcEngine.setLocalVoiceEqualization(4, option[4]);
    rtcEngine.setLocalVoiceEqualization(5, option[5]);
    rtcEngine.setLocalVoiceEqualization(6, option[6]);
    rtcEngine.setLocalVoiceEqualization(7, option[7]);
    rtcEngine.setLocalVoiceEqualization(8, option[8]);
    rtcEngine.setLocalVoiceEqualization(9, option[9]);

    // 原始声音效果，即所谓的 dry signal，取值范围 [-20, 10]，单位为 dB
    rtcEngine.setLocalVoiceReverb(AudioConst.REVERB_DRY_LEVEL, option[10]);

    // 早期反射信号效果，即所谓的 wet signal，取值范围 [-20, 10]，单位为 dB
    rtcEngine.setLocalVoiceReverb(AudioConst.REVERB_WET_LEVEL, option[11]);

    // 所需混响效果的房间尺寸，一般房间越大，混响越强，取值范围 [0, 100]，单位为 dB
    rtcEngine.setLocalVoiceReverb(AudioConst.REVERB_ROOM_SIZE, option[12]);

    // Wet signal 的初始延迟长度，取值范围 [0, 200]，单位为毫秒
    rtcEngine.setLocalVoiceReverb(AudioConst.REVERB_WET_DELAY, option[13]);

    // 混响持续的时间，取值范围为 [0, 100]，单位为毫秒
    rtcEngine.setLocalVoiceReverb(AudioConst.REVERB_STRENGTH, option[14]);
```
### API参考
* [setLocalVoicePitch()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a41b525f9cbf2911594bcda9b20a728c9)
* [setLocalVoiceEqualization()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a9e3aa79f0d6d8f2ea81907543506d960)
* [setLocalVoiceReverb()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a4afc32ba68e997e90ba3f128317827fa)

### 开发注意事项
* 所有相关的API都有返回值。返回值小于0表示API调用失败

## 视频编码属性设置
setVideoEncoderConfiguration() 方法用来设置视频相关的属性，比如分辨率、码率、帧率等。参数均为理想情况下的最大值。当视频引擎因网络环境等原因无法达到设置的分辨率、帧率或码率的最大值时，会取最接近最大值的那个值。
### 实现方法
```Java
	// 首先配置一个VideoEncoderConfiguration实例
	// 参数请到API参考中的链接文档查看
	VideoEncoderConfiguration config = new VideoEncoderConfiguration(
		VideoDimensions.VD_640x480,  // 可以选择默认的几种分辨率选项，也可以自定义
		FRAME_RATE_FPS_15,           // 帧率，可自定义。通常建议是15帧，不超过30帧
		STANDARD_BITRATE,            // 标准码率，具体的参数配置请参照 VideoEncoderConfiguration文档中关于bitrate的说明。也可以配置其它的码率值，但一般情况下推荐使用标准码率。
		ORIENTATION_MODE_ADAPTIVE    // 方向模式，请参照API参考的链接查看详细的说明
	);

	rtcEngine.setVideoEncoderConfiguration(config);
```
### API参考
* [setVideoEncoderConfiguration()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f)
* [Video Encoder Configuration](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1video_1_1_video_encoder_configuration.html)
* [Orientation Mode](https://docs.agora.io/cn/Video/API%20Reference/java/enumio_1_1agora_1_1rtc_1_1video_1_1_video_encoder_configuration_1_1_o_r_i_e_n_t_a_t_i_o_n___m_o_d_e.html)

### 开发注意事项
* 如果用户加入频道后不需要重新设置视频编码属性, 建议在enableVideo前调用setVideoEncoderConfiguration()，可以加快首帧出图的时间
* 所有相关的API都有返回值。返回值小于0表示API调用失败

## 摄像头对焦
### 实现方法
``` Java
	// 检测当前设备是否支持人脸自动对焦并设置
	boolean shouldSetFaceMode = rtcEngine.isCameraAutoFocusFaceModeSupported();
	rtcEngine.setCameraAutoFocusFaceModeEnabled(shouldSetFaceMode);
	
	// 检测设备是否支持手动对焦功能并设置
	boolean shouldManualFocus = rtcEngine.isCameraFocusSupported();
	if (shouldManualFocus) {
		// 假设在屏幕(50, 100)的位置对焦
		float positionX = 50.0f;
		float positionY = 100.0f;
		rtcEngine.setCameraFocusPositionInPreview(positionX, positionY);
	}
	
```
### API参考
* [isCameraFocusSupported()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a0e20f04ccecfc41aa23bf63116c9a8cd)
* [isCameraAutoFocusFaceModeSupported](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a09f61f738cf7d8a1902761e03a7fa600)
* [setCameraFocusPositionInPreview()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#aba273e4337a760d883b6c7c1344183c0)
* [setCameraAutoFocusFaceModeEnabled()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a7e67afe7ad0045448fe0bd97203afcee)

## 获取/修改原始音视频数据
请参考 [修改音视频原始数据](https://docs.agora.io/cn/Interactive%20Broadcast/rawdata_android?platform=Android)

## 客户端自定义采集和渲染
请参考 [自定义视频源和渲染器](https://docs.agora.io/cn/Interactive%20Broadcast/custom_video_android?platform=Android)

## 进行屏幕共享
屏幕共享是先通过 MediaProjection/VirtualDisplay 拿到屏幕数据。然后通过 pushExternalVideoFrame 接口给到 Agora SDK，在进频道之前要先设置 setExternalVideoSource
### 注意事项
* 具体细节请参考 [demo](https://github.com/AgoraIO/Advanced-Video/tree/master/Screensharing/Agora-Screen-Sharing-Android)

## 推流到CDN
请参考 [推流到CDN](https://docs.agora.io/cn/Interactive%20Broadcast/push_stream_android2.0?platform=Android)

## 在线媒体流输入
请参考 [输入在线流媒体](https://docs.agora.io/cn/Interactive%20Broadcast/inject_stream_android?platform=Android)

## 数据包加密
请参考文档 [选择加密方案](https://docs.agora.io/cn/Video/encryption_android_agora?platform=Android)

## 改善弱网络环境下的用户体验
暂不支持

## 通话录制
Agora SDK 支持通话过程中在客户端进行录音。该方法录制频道内所有用户的音频，并生成一个包含所有用户声音的录音文件，录音文件格式可以为：

* wav：文件大，音质保真度高
* aac：文件小，有一定的音质损失
### 实现方法
```Java
	// 开始录音
	rtcEngine.startAudioRecording(
		"path/to/file",                   // 录音文件的本地保存路径，由用户自行指定，需精确到文件名及格式
		AUDIO_RECORDING_QUALITY_HIGH      // 录音音质，分LOW, MEDIUM, HIGH
	);

	// 结束录音
	rtcEngine.stopAudioRecording();
```
### API参考
* [startAudioRecording()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a44744695d723b7d18c704a57f828cddb)
* [stopAudioRecording()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a2d751055a21611b3cf99fe39d24bb1a0)
### 注意事项
* 开启录音须在进入频道之后调用
* 离开频道会自动停止录音
* 所有相关的API都有返回值。返回值小于0表示API调用失败

## 屏幕旋转模式

* 请参考 [视频采集旋转](https://docs.agora.io/cn/Interactive%20Broadcast/rotation_guide_android?platform=Android)。文档里包含了一些图示，帮助开发者感性理解旋转方向的原理

## 实现4人以上视频通话
* 请参阅demo https://github.com/AgoraIO/Advanced-Video/tree/master/Large-Group-Video-Chat
* 请参考 [实现七人以上视频通话](https://docs.agora.io/cn/Interactive%20Broadcast/seventeen_people_android?platform=Android)

## 移动端、桌面端、web端互通
### 注意事项
* 初始化配置详情可参考[官方文档](https://docs.agora.io/cn/Interactive%20Broadcast/API%20Reference/web/interfaces/agorartc.clientconfig.html)

## 音视频设备测试
是测试系统的音频设备（耳麦、扬声器等）和网络连接是否正常。在测试过程中，用户先说一段话，在 10 秒后，声音会回放出来。如果 10 秒后用户能正常听到自己刚才说的话，就表示系统音频设备和网络连接都是正常的。
### 实现方法
```Java
	// 开启回声测试
	rtcEngine.startEchoTest();

	// 等待并检查是否可以听到自己的声音回放

	// 停止测试
	rtcEngine.stopEchoTest();
```
### API参考
* [startEchoTest()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#ac93b84c9ebbb32f5ee304732804ec1b9)
* [stopEchoTest()](https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a01b8067275003c011f6d81bb41ee0fe1)

### 注意事项
* 调用 startEchoTest 后必须调用 stopEchoTest 以结束测试，否则不能进行下一次回声测试，或者调用 joinchannel 进行通话
* 直播模式下，该方法仅能由用户角色为主播的用户调用。如果用户由通信模式切换到直播模式，请务必调用 setClientRole 方法将用户角色设置为主播后再调用该方法
* 所有相关的API都有返回值。返回值小于0表示API调用失败
