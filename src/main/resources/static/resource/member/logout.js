// button을 매개인자로 받지 않으면, 이 함수와 해당 태그가 서로 인식하지 못한다.
function MemberLogout__submit(button) {
    $.ajax({
        url: '/usr/member/doLogout',
        success: function(){
            alert('로그아웃되었습니다.');
            window.location.replace('/usr/home/main');
        }

    });
}
