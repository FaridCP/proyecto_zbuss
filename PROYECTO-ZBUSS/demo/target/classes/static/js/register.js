    document.addEventListener('DOMContentLoaded', function() {
            // Toggle password visibility
            const togglePassword = document.getElementById('togglePassword');
            const passwordInput = document.getElementById('clave');
            
            if (togglePassword && passwordInput) {
                togglePassword.addEventListener('click', function() {
                    const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                    passwordInput.setAttribute('type', type);
                    
                    // Change the icon
                    const icon = this.querySelector('i');
                    if (type === 'password') {
                        icon.classList.remove('fa-eye-slash');
                        icon.classList.add('fa-eye');
                    } else {
                        icon.classList.remove('fa-eye');
                        icon.classList.add('fa-eye-slash');
                    }
                });
            }
            
            // Password strength meter
            document.getElementById('clave').addEventListener('input', function() {
                const password = this.value;
                const strengthMeter = document.getElementById('passwordStrength');
                const reqLength = document.getElementById('reqLength');
                const reqNumber = document.getElementById('reqNumber');
                const reqLetter = document.getElementById('reqLetter');
                
                // Reset requirements
                reqLength.classList.remove('valid');
                reqNumber.classList.remove('valid');
                reqLetter.classList.remove('valid');
                
                let strength = 0;
                let color = '#dc3545'; // Red
                
                // Check length
                if (password.length >= 8) {
                    strength += 33;
                    reqLength.classList.add('valid');
                }
                
                // Check for numbers
                if (/\d/.test(password)) {
                    strength += 33;
                    reqNumber.classList.add('valid');
                }
                
                // Check for letters
                if (/[a-zA-Z]/.test(password)) {
                    strength += 34;
                    reqLetter.classList.add('valid');
                }
                
                // Update strength meter
                strengthMeter.style.width = strength + '%';
                
                // Change color based on strength
                if (strength >= 66) {
                    color = '#ffc107'; // Yellow
                }
                if (strength >= 99) {
                    color = '#198754'; // Green
                }
                
                strengthMeter.style.backgroundColor = color;
            });
            
            // Form validation and effects
            const form = document.getElementById('registerForm');
            if (form) {
                form.addEventListener('submit', function(e) {
                    let valid = true;
                    
                    // Reset invalid classes
                    const inputs = form.querySelectorAll('input, select');
                    inputs.forEach(input => {
                        input.classList.remove('is-invalid');
                        input.classList.remove('shake');
                    });
                    
                    // Check name
                    const nameInput = document.getElementById('nombre');
                    if (nameInput.value.trim() === '') {
                        valid = false;
                        nameInput.classList.add('is-invalid');
                    }
                    
                    // Check tipoDocumento
                    const tipoDocumentoSelect = document.getElementById('tipoDocumento');
                    if (!tipoDocumentoSelect.value) {
                        valid = false;
                        tipoDocumentoSelect.classList.add('is-invalid');
                    }
                    
                    // Check documento
                    const documentoInput = document.getElementById('documento');
                    if (documentoInput.value.trim() === '') {
                        valid = false;
                        documentoInput.classList.add('is-invalid');
                    }
                    
                    // Check email
                    const emailInput = document.getElementById('correo');
                    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    if (!emailRegex.test(emailInput.value)) {
                        valid = false;
                        emailInput.classList.add('is-invalid');
                    }
                    
                    // Check telefono
                    const telefonoInput = document.getElementById('telefono');
                    if (telefonoInput.value.trim() === '') {
                        valid = false;
                        telefonoInput.classList.add('is-invalid');
                    }
                    
                    // Check password
                    const passwordInput = document.getElementById('clave');
                    if (passwordInput.value.length < 8) {
                        valid = false;
                        passwordInput.classList.add('is-invalid');
                    }
                    
                    // Check terminos
                    const terminosCheckbox = document.getElementById('terminos');
                    if (!terminosCheckbox.checked) {
                        valid = false;
                        terminosCheckbox.classList.add('is-invalid');
                    }
                    
                    if (!valid) {
                        e.preventDefault();
                        
                        // Add shake animation to invalid fields
                        const invalidFields = form.querySelectorAll('.is-invalid');
                        invalidFields.forEach(field => {
                            field.classList.add('shake');
                            setTimeout(() => {
                                field.classList.remove('shake');
                            }, 600);
                        });
                    } else {
                        // Simulate form submission
                        e.preventDefault();
                        alert('¡Registro exitoso! Su cuenta ha sido creada correctamente.');
                        form.reset();
                    }
                });
            }
            
            // Input focus effects
            const inputs = document.querySelectorAll('input, select');
            inputs.forEach(input => {
                input.addEventListener('focus', function() {
                    this.parentElement.classList.add('focused');
                });
                
                input.addEventListener('blur', function() {
                    this.parentElement.classList.remove('focused');
                });
            });
        });