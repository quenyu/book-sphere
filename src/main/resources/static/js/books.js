let currentPage = 0;
const pageSize = parseInt(document.querySelector('#books-container').getAttribute('data-size') || '20');
const container = document.getElementById('books-container');
const loading = document.getElementById('loading-indicator');

window.addEventListener('scroll', () => {
    if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight - 100) {
        loadNextPage();
    }
});

let loadingInProgress = false;
function loadNextPage() {
    if (loadingInProgress) return;
    loadingInProgress = true;
    loading.classList.remove('hidden');
    currentPage += 1;

    fetch(`/api/v1/books?page=${currentPage}&size=${pageSize}`, {
        credentials: 'same-origin'
    })
        .then(res => res.json())
        .then(page => {
            page.content.forEach(book => {
                const a = document.createElement('a');
                a.href = `/books/${book.id}/read`;
                a.className = 'group book-card';
                a.innerHTML = `
          <div class="book-cover rounded-lg mb-3 transition-all duration-300 group-hover:opacity-90"
               style="background-image: url('${book.coverUrl}');"></div>
          <h3 class="book-title text-md font-semibold mt-2">${book.title}</h3>
          <p class="book-author text-sm text-gray-400">${book.author}</p>
        `;
                container.appendChild(a);
            });
            if (page.last) {
                window.removeEventListener('scroll', this);
            }
        })
        .catch(err => console.error('Failed to load page', err))
        .finally(() => {
            loading.classList.add('hidden');
            loadingInProgress = false;
        });
}
