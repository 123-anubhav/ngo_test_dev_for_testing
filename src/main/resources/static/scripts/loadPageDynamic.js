<!-- JavaScript to handle dynamic loading of header content -->
document.addEventListener('DOMContentLoaded', function() {
        var link = document.getElementById('loadHeaderContent');
        if (link) {
            var url = link.getAttribute('href');
            fetch(url)
                .then(response => response.text())
                .then(html => {
                    document.getElementById('headerContentPlaceholder').innerHTML = html; // Inject HTML into header placeholder
                })
                .catch(error => console.error('Error loading header content:', error));
        }
    });