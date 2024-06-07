const deleteButton = document.querySelector('#delete-btn')

if (deleteButton) {
    deleteButton.addEventListener('click', (e) => {
        let id = document.getElementById('article-id').value;
        fetch(`/api/articles/${id}`, {method: 'DELETE'})
            .then(() => {
                alert('Deleted!');
                location.replace('/articles');
            });
    });
}

const modifyButton = document.querySelector('#modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        fetch(`/api/articles/${id}`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
            .then(() => {
                alert('수정이 완료되었습니다.');
                location.replace(`/articles/${id}`);
            });
    });
}
        const createButton = document.querySelector('#create-btn');

    if (createButton) {
        createButton.addEventListener('click', (e) => {
            fetch("/api/articles", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    title: document.getElementById('title').value,
                    content: document.getElementById('content').value,
                }),
            }).then(() => {
                alert("등록 완료.");
                location.replace("/articles");
            });
        });
}