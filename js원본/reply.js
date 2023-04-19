$(document).ready(function () {
    $('.reply-toggle').click(function () {
        var $this = $(this);
        var $parent = $this.closest('.card-body');
        $parent.find('.reply-form').toggleClass('d-none');
    });
    $(document).on('submit', 'form',function(e) {
        e.preventDefault();
        var btn = e.target.querySelector('button');
        btn.disabled = true;

        var url;
        switch ($(this).attr('id')) {
            case 'board-del' : {url = '/whiteboard/delete'; break;}
            case 'comment-add' : {url = '/whiteview/comment/add'; break;}
            case 'comment-edit' : {url = '/whiteview/comment/edit'; break;}
            case 'comment-del' : {url = '/whiteview/comment/delete'; break;}
            case 'reply-add' : {url = '/whiteview/reply/add'; break;}
            case 'reply-edit' : {url = '/whiteview/reply/edit'; break;}
            case 'reply-del' : {url = '/whiteview/reply/delete'; break;}
        }
        var data = $(this).serialize();
        $.ajax({
            url: url,
            method: 'POST',
            data: data,
            success: function(response) {
                if (response !== '') {
                    location.href = response;
                } else {
                    location.reload();
                }
                btn.disabled = false;
            },
            error: function(xhr, status, error) {
                location.href = '/error';
            },
        });
    });
});

// 수정 버튼을 클릭할 때 (댓글폼)
const editBtn = document.querySelectorAll('.edit-comment');
editBtn.forEach(editBtn => {
    editBtn.addEventListener('click', event => {
        const comment = event.target.closest('.card-body').querySelector('.edit-comment-contents');
        const commentText = comment.innerText;

        const form = document.createElement('form');
        form.setAttribute('method', 'post');
        form.setAttribute('id', 'comment-edit');
        form.className = 'comment-box'

        const textArea = document.createElement('textarea');
        textArea.setAttribute('name', 'contents');
        textArea.className = 'form-control reply-text';
        textArea.textContent = commentText;

        const input = document.createElement('input');
        input.setAttribute('name', 'cid');
        input.setAttribute('type', 'hidden');
        input.value = event.target.closest('.card-body').querySelector('.cid').textContent;

        const btn = document.createElement('button');
        btn.className = 'btn btn-primary btn-sm m-2';
        btn.textContent = '저장';

        form.appendChild(textArea);
        form.appendChild(input)
        form.appendChild(btn);

        const cancelBtn = document.createElement('button');
        cancelBtn.setAttribute('type', 'button');
        cancelBtn.className = 'btn btn-sm btn-secondary m-2';
        cancelBtn.innerText = '취소';
        form.appendChild(cancelBtn);

        // 댓글을 폼으로 교체
        const commentContainer = comment.parentElement;
        commentContainer.replaceChild(form, comment);
        // 취소 버튼을 클릭할 때
        cancelBtn.addEventListener('click', () => {
            // 댓글 폼을 댓글 요소로 교체
            commentContainer.replaceChild(comment, form);
        });
    });
});

// 수정 버튼을 클릭할 때 (대댓글)
const editBtn2 = document.querySelectorAll('.edit-reply');
editBtn2.forEach(editBtn2 => {
    editBtn2.addEventListener('click', event => {
        const reply = event.target.closest('.card-body').querySelector('.edit-reply-contents');
        const commentText = reply.innerText;

        const form = document.createElement('form');
        form.setAttribute('method', 'post');
        form.setAttribute('id', 'reply-edit');
        form.className = 'reply-box'

        const textArea2 = document.createElement('textarea');
        textArea2.setAttribute('name', 'contents');
        textArea2.className = 'form-control reply-text';
        textArea2.textContent = commentText;

        const btn2 = document.createElement('button');
        btn2.className = 'btn btn-primary btn-sm m-2';
        btn2.textContent = '저장';

        const input2 = document.createElement('input');
        input2.setAttribute('name', 'rid');
        input2.setAttribute('type', 'hidden');
        input2.value = event.target.closest('.card-body').querySelector('.rid').textContent;

        form.appendChild(textArea2);
        form.appendChild(input2);
        form.appendChild(btn2);

        const cancelBtn2 = document.createElement('button');
        cancelBtn2.setAttribute('type', 'button');
        cancelBtn2.className = 'btn btn-sm btn-secondary m-2';
        cancelBtn2.innerText = '취소';
        form.appendChild(cancelBtn2);

        // 댓글을 폼으로 교체
        const commentContainer = reply.parentElement;
        commentContainer.replaceChild(form, reply);

        cancelBtn2.addEventListener('click', () => {
            commentContainer.replaceChild(reply, form);
        });
    });
});