// Animación al hacer scroll
document.addEventListener('DOMContentLoaded', function() {
  const fadeElements = document.querySelectorAll('.fade-in');
  
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.style.opacity = 1;
        entry.target.style.visibility = 'visible';
      }
    });
  }, { threshold: 0.1 });
  
  fadeElements.forEach(element => {
    element.style.opacity = 0;
    element.style.visibility = 'hidden';
    element.style.transition = 'opacity 0.8s ease, visibility 0.8s ease';
    observer.observe(element);
  });
});