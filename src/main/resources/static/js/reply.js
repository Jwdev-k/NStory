$(document).ready(function() {
    $('.reply-toggle').click(function(e) {
        e.preventDefault();
        var $this = $(this);
        var $parent = $this.closest('.card-body');
        $parent.find('.reply-form').toggleClass('d-none');
    });
});

// 수정 버튼을 클릭할 때 (댓글폼)
const editBtn = document.querySelector('.edit-comment');
editBtn.addEventListener('click', () => {
    const comment = document.getElementById("edit-comment");
    const commentText = comment.innerText;

    const form = document.createElement('form');
    form.setAttribute('method', 'post');
    form.setAttribute('action', '/whiteview/comment/edit');

    const textArea = document.createElement('textarea');
    textArea.className = 'form-control reply-text';
    textArea.textContent = commentText;
    textArea.style.resize = 'none';

    const btn = document.createElement('button');
    btn.className = 'btn btn-primary btn-sm m-2';
    btn.textContent = '저장';

    form.appendChild(textArea);
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

// 수정 버튼을 클릭할 때 (대댓글)
const editBtn2 = document.querySelector('.edit-reply');
editBtn2.addEventListener('click', () => {
    const reply = document.getElementById("edit-reply");
    const commentText = reply.innerText;

    const form = document.createElement('form');
    form.setAttribute('method', 'post');
    form.setAttribute('action', '/whiteview/reply/edit');

    const textArea2 = document.createElement('textarea');
    textArea2.className = 'form-control reply-text';
    textArea2.textContent = commentText;
    textArea2.style.resize = 'none';

    const btn2 = document.createElement('button');
    btn2.className = 'btn btn-primary btn-sm m-2';
    btn2.textContent = '저장';

    form.appendChild(textArea2);
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

