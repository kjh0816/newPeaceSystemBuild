// button을 매개인자로 받지 않으면, 이 함수와 해당 태그가 서로 인식하지 못한다.
function MemberLogout__submit(button) {
    $.ajax({
                url: '../member/doLogout',
                success: function(){
                    alert('로그아웃되었습니다.');
//                  로그인 페이지를 절대경로로 이동
                    location.replace('/usr/member/login');
                }

            });
}
