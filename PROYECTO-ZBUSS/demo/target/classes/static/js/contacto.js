document.addEventListener('DOMContentLoaded', function() {
    const contactForm = document.getElementById('contactForm');
    
    if (contactForm) {
        contactForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Validación simple
            const nombre = document.getElementById('nombre');
            const email = document.getElementById('email');
            const mensaje = document.getElementById('mensaje');
            let isValid = true;
            
            // Resetear errores
            document.querySelectorAll('.form-group').forEach(group => {
                group.classList.remove('has-error');
            });
            
            // Validar nombre
            if (!nombre.value.trim()) {
                nombre.parentElement.classList.add('has-error');
                isValid = false;
            }
            
            // Validar email
            if (!email.value.trim() || !/^\S+@\S+\.\S+$/.test(email.value)) {
                email.parentElement.classList.add('has-error');
                isValid = false;
            }
            
            // Validar mensaje
            if (!mensaje.value.trim()) {
                mensaje.parentElement.classList.add('has-error');
                isValid = false;
            }
            
            if (isValid) {
                // Aquí iría la lógica para enviar el formulario
                alert('¡Gracias por tu mensaje! Nos pondremos en contacto contigo pronto.');
                contactForm.reset();
                
                // Animación de éxito
                const submitBtn = document.querySelector('.submit-btn');
                submitBtn.innerHTML = '<i class="fas fa-check"></i> Mensaje enviado';
                submitBtn.style.backgroundColor = '#4BB543';
                
                setTimeout(() => {
                    submitBtn.innerHTML = '<i class="fas fa-paper-plane"></i> Enviar mensaje';
                    submitBtn.style.backgroundColor = '';
                }, 3000);
            } else {
                // Animación de error
                const errorElements = document.querySelectorAll('.has-error');
                errorElements.forEach(el => {
                    el.style.animation = 'shake 0.5s';
                    setTimeout(() => {
                        el.style.animation = '';
                    }, 500);
                });
            }
        });
    }
    
    // Agregar máscara para el teléfono si es necesario
    const phoneInput = document.getElementById('telefono');
    if (phoneInput) {
        phoneInput.addEventListener('input', function(e) {
            // Eliminar todo lo que no sea número
            this.value = this.value.replace(/[^\d]/g, '');
        });
    }
});

// Animación shake
const style = document.createElement('style');
style.textContent = `
    @keyframes shake {
        0%, 100% { transform: translateX(0); }
        25% { transform: translateX(-5px); }
        75% { transform: translateX(5px); }
    }
    
    .has-error input,
    .has-error select,
    .has-error textarea {
        border-color: #f72585 !important;
    }
    
    .has-error label {
        color: #f72585 !important;
    }
`;
document.head.appendChild(style);