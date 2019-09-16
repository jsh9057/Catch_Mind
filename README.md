# Catch_Mind
Android Project
개발날짜 2017/11~2017/12
-------------------------------------------------------
-목차-

[a. 개발](#개발)

[b. 실행화면](#실행화면)

[c. Issue](#Issue)

## 개발
1. 프로젝트 제작배경
- 통신량이 많은 TCP/IP 통신과 AWS를 이용한 웹 서버 구축 및 http통신을 이용한 DB제어 목적.

2. 프로젝트 벤치마킹
- 넷마블 CatchMind

3. 기본 게임 방법
- 회원가입을 통한 아이디 생성
- 생성된 아이디로 게임에 접속 시 바로 게임방에 입장.
- 맨 처음 게임방에 접속한 유저가 출제자가 된다.
- 출제자는 그림을 그리려서 다른 플레이어들이 맞출 수 있게 한다.
- 정답을 맞춘 플레이어는 출제자가 된다.

4. Architecture

![image](https://user-images.githubusercontent.com/31503178/64949865-1e448d00-d8b5-11e9-92d3-5ff4034a01ea.png)

- Catch mind는 실시간으로 그림의 점 좌표값(이하 point)를 대량으로 주고받아야 합니다. 때문에 최대한으로 socket server의 과부하를 줄이기 위해 data server와 socket server를 나누었습니다. socket server에서는 채팅, point, system massage를 실시간으로 주고 받습니다. 
 Web server & data server 는 AWS에 생성한 클라우드 OS이며 이 OS에 Mysql과 python 기반의 웹 프레임 워크인 django를 이용하여 구축하였습니다. django는 spring과 유사한 웹 프레임 워크이며 이를 이용해 웹 서버를 구축할 수 있습니다. 구축된 웹서버를 이용해 mysql에 접근하여 클라이언트들과 http통신을 하여 데이터를 주고받습니다.

5. 주요 알고리즘

 << sendThread 의 일부 >>
 public void run() {
  try {
 while(output!=null){
   now_pointlist_size=drawboard.point_list.size();
   if(old_pointlist_size!=now_pointlist_size && drawboard.draw_permission)
{mop=newObject_mop(nick,"point",drawboard.point_list.get(drawboard.point  _list.size()-1));
   output.writeUTF(mop.toString());
   output.flush();
    }
   old_pointlist_size=now_pointlist_size;
 - point_list의 size값의 변동이 있을 때만 보내는 방식으로 구현.

 << receiverThread 의 일부 >>
  if(mop.kind.equals("point")&&!mop.nick.equals(drawboard.whodraw)){
     drawboard.point_list.add(mop.getp());
     drawboard.postInvalidate();
     }

- point는 sender를 해준 클라이언트에게 다시 보내주면 안되기 때문에

- permission을 두어 현재 그림을 그릴 권한이 없을 경우 Drawboard의 click listener 가 비활성화.



## 실행화면

-로그인화면-

![image](https://user-images.githubusercontent.com/31503178/64949672-ab3b1680-d8b4-11e9-9f16-fedd3c001679.png)

- 회원가입( 중복된 아이디로 가입시도 시) -

![image](https://user-images.githubusercontent.com/31503178/64949689-b4c47e80-d8b4-11e9-864f-af063c9541ae.png)

- 아이디를 입력하지 않았을때와, 두 비밀번호가 일치하지 않을 때-

![image](https://user-images.githubusercontent.com/31503178/64949693-b7bf6f00-d8b4-11e9-9633-a1f7a57dada5.png)

- 게임화면 1 -

![image](https://user-images.githubusercontent.com/31503178/64949791-f0f7df00-d8b4-11e9-878c-d7315c519a21.png)

- 게임화면 2 -

![image](https://user-images.githubusercontent.com/31503178/64949811-fead6480-d8b4-11e9-8b43-4575f556fe97.png)


## ISSUE

<<목적>>
android studio에서 작성한 객체를 Serializable를 통해 eclipse 상에 만든 TCP서버로 객체를 넘기는 것.
문제점 : ObjectInputstream 의 생성자가 생성되지 않음.

 - 원인 : android studio에서 만든 직렬화를 할 객체 클래스의 패키지 명과 경로가
         eclipse에서 같은 직렬화 할 객체 클래스의 패키지 명과 경로가 다르기 때문.

<<진행경과1>>

![image](https://user-images.githubusercontent.com/31503178/64950601-b2fbba80-d8b6-11e9-934a-388f5d8ff04f.png)

eclipse의 패키지의 이름과 경로를 android studio 패키지의 이름과 경로에 맞추었으나 이번엔 NullpointException 오류가 나옴. 보내기 전 객체의 정보를 각각 출력하여 확인 하였으나 정상 적으로 출력됨.

<<진행경과2>>

![image](https://user-images.githubusercontent.com/31503178/64950633-ceff5c00-d8b6-11e9-8e6c-cc487cd1788c.png)

문제점을 찾는 도중 serialVersionUID를 알게됨. 클라이언트의 클래스와 서버측의 클래스의 
serialVersionUID 값을 맞춰 줌으로써, 직렬화가 가능해짐.

![image](https://user-images.githubusercontent.com/31503178/64950649-db83b480-d8b6-11e9-9869-923cfeedb9a6.png)

클라이언트 측에서 보낸 닉네임을 서버 측에서 받고, 서버에서 보낸 객체를 클라이언트에서 받는 것이 가능해짐.

<<진행경과3>>

정상적으로 통신이 되는 것 같았지만, 클라이언트 측에서 다시 객체를 전송할 시 
11-20 17:02:46.854 15736-15736/com.example.jeong.android_project E/AndroidRuntime: FATAL EXCEPTION: main
Process: com.example.jeong.android_project, PID: 15736
android.os.NetworkOnMainThreadException
at android.os.StrictMode$AndroidBlockGuardPolicy.onNetwork(StrictMode.java:1303)
at java.net.SocketOutputStream.socketWrite(SocketOutputStream.java:111)
at java.net.SocketOutputStream.write(SocketOutputStream.java:157)
at java.io.ObjectOutputStream$BlockDataOutputStream.drain(ObjectOutputStream.java:1946)
at java.io.ObjectOutputStream$BlockDataOutputStream.setBlockDataMode(ObjectOutputStream.java:1833)
at java.io.ObjectOutputStream.<init>(ObjectOutputStream.java:246)
at com.example.jeong.android_project.MainActivity$SendThread.<init>(MainActivity.java:201)
at com.example.jeong.android_project.MainActivity$3.onClick(MainActivity.java:100)

![image](https://user-images.githubusercontent.com/31503178/64950670-ea6a6700-d8b6-11e9-8c73-5831dba49cb7.png)

objectOutputStream = new ObjectoutputStream(socket.getOutputStream()); 
에서 에러가 나옴. 서버측에서 클라이언트의 객체를 보내는 것은 아무 문제없이 잘 됨.

![image](https://user-images.githubusercontent.com/31503178/64950676-edfdee00-d8b6-11e9-9a8c-3192daaac978.png)

<<진행경과4>>

client의 Objectoutput문제로 추측.
- ObjectInputStream을 양쪽에서 먼저 생성하면 blocking 모드로 빠진다는 것.
       But : 양쪽 모두 ObjectOutputStream의 생성자가 먼저 생성되게 설계함.

- 처음 클라이언트 측에서 nick을 보낼시 OutputStream이 flash() 되지않은 경우.
   flash()가 되지 않을 경우 blocking모드로 빠질 수 있음.
       But : 클라이언트측의 첫 통신시 outputstream을 flash() 해주었으며,
             SendThread의 생성자에서 objectOutputStream을 사용하기전에도 flash()을                해주었으나 달라짐이없음.
  판단근거 : at java.io.ObjectOutputStream$BlockDataOutputStream.drain(ObjectOutputStream.java:1946)
　　　　　　　　　　at java.io.ObjectOutputStream$BlockDataOutputStream.setBlockDataMode(ObjectOutputStream.java:1833)

<<해결>>

모든 객체를 String 으로 바꾸어 보내고, 받을때는 String을 객체화 함.



