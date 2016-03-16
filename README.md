##WeiXinCamera
仿微信小视频录像

仿微信实现小视频录制界面,主要实现了按住拍这个功能,视频录制的分辨率和视频大小并不是很好.

自定义textView,重写onLongClick方法,抓取ACTION_UP事件,控制视频的拍摄

在使用Camera时发现了一个很诡异的bug, 在调用Camera的release()方法时,注意如果连续调用多次release()时,java虚拟机会崩溃出错,

主要原因在于java虚拟机在调用系统摄像头时,如果调用不正常,就会出错崩溃,这个异常不是exception, 不是error, 而是java虚拟机底层的异常

所以这个异常是抓取不了的,而且log日志也不会打印,错误也很难发现,所以在调用有关系统硬件的方法时,一定要谨慎

ps:代码只提供思路

![image](https://github.com/Zhaoss/WeiXinCamera/blob/master/image/1.jpg?raw=true)
![image](https://github.com/Zhaoss/WeiXinCamera/blob/master/image/2.jpg?raw=true)
