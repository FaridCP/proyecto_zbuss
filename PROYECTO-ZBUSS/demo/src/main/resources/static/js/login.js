document.addEventListener('DOMContentLoaded', function() {
    // Inicializar partículas
    particlesJS('particles-js', {
        particles: {
            number: {
                value: 80,
                density: {
                    enable: true,
                    value_area: 800
                }
            },
            color: {
                value: "#4361ee"
            },
            shape: {
                type: "circle",
                stroke: {
                    width: 0,
                    color: "#000000"
                }
            },
            opacity: {
                value: 0.5,
                random: true,
                anim: {
                    enable: true,
                    speed: 1,
                    opacity_min: 0.1,
                    sync: false
                }
            },
            size: {
                value: 3,
                random: true,
                anim: {
                    enable: true,
                    speed: 2,
                    size_min: 0.1,
                    sync: false
                }
            },
            line_linked: {
                enable: true,
                distance: 150,
                color: "#4361ee",
                opacity: 0.3,
                width: 1
            },
            move: {
                enable: true,
                speed: 1,
                direction: "none",
                random: true,
                straight: false,
                out_mode: "out",
                bounce: false,
                attract: {
                    enable: true,
                    rotateX: 600,
                    rotateY: 1200
                }
            }
        },
        interactivity: {
            detect_on: "canvas",
            events: {
                onhover: {
                    enable: true,
                    mode: "grab"
                },
                onclick: {
                    enable: true,
                    mode: "push"
                },
                resize: true
            },
            modes: {
                grab: {
                    distance: 140,
                    line_linked: {
                        opacity: 0.7
                    }
                },
                push: {
                    particles_nb: 4
                }
            }
        },
        retina_detect: true
    });

    // Función para mostrar/ocultar contraseña
    window.togglePassword = function() {
        const passwordInput = document.getElementById('contrasena');
        const toggleIcon = document.querySelector('.toggle-password');
        
        if (passwordInput.type === 'password') {
            passwordInput.type = 'text';
            toggleIcon.classList.remove('fa-eye');
            toggleIcon.classList.add('fa-eye-slash');
        } else {
            passwordInput.type = 'password';
            toggleIcon.classList.remove('fa-eye-slash');
            toggleIcon.classList.add('fa-eye');
        }
    };

    // Efecto hover en el botón de login
    const loginBtn = document.querySelector('.btn-login');
    if (loginBtn) {
        loginBtn.addEventListener('mouseenter', function() {
            this.querySelector('.btn-text').style.transform = 'translateX(-10px)';
            this.querySelector('.btn-icon').style.transform = 'translateX(0)';
            this.querySelector('.btn-icon').style.opacity = '1';
        });
        
        loginBtn.addEventListener('mouseleave', function() {
            this.querySelector('.btn-text').style.transform = 'translateX(0)';
            this.querySelector('.btn-icon').style.transform = 'translateX(30px)';
            this.querySelector('.btn-icon').style.opacity = '0';
        });
    }

    // Validación del formulario
    const loginForm = document.querySelector('.login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            let isValid = true;
            const email = document.getElementById('correo');
            const password = document.getElementById('contrasena');
            
            // Validar email
            if (!email.value || !/^\S+@\S+\.\S+$/.test(email.value)) {
                isValid = false;
                email.classList.add('is-invalid');
            } else {
                email.classList.remove('is-invalid');
            }
            
            // Validar contraseña
            if (!password.value || password.value.length < 6) {
                isValid = false;
                password.classList.add('is-invalid');
            } else {
                password.classList.remove('is-invalid');
            }
            
            if (!isValid) {
                e.preventDefault();
                // Agregar animación de shake a los campos inválidos
                const invalidFields = document.querySelectorAll('.is-invalid');
                invalidFields.forEach(field => {
                    field.style.animation = 'shake 0.5s';
                    setTimeout(() => {
                        field.style.animation = '';
                    }, 500);
                });
            }
        });
    }
});