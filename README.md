# Catch_Mind
Android Project
개발날짜 2017/11~2017/12
-------------------------------------------------------
목차
a. 개발(#개발)
b. 실행화면(#실행화면)
c. 이슈(#이슈)

##개발
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



##실행화면

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


##이슈

