<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Your Profile | Aurora Store</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/portfolio.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <div class="page-container">
        <%@ include file="header.jsp" %>
        
        <main class="profile-main">
            <div class="profile-container">
                <div class="profile-header">
                    <h1>Welcome to your profile, ${user.user_name}!</h1>
                </div>
                
                <div class="profile-content">
                    <div class="profile-image-container">
                        <div class="profile-image">
                            <img src="${pageContext.request.contextPath}/${imagePath}" alt="Profile Image" 
                                 onerror="this.src='${pageContext.request.contextPath}/resources/images/system/default-profile.png'">
                            <div class="image-overlay">
                                <button class="edit-image-btn" onclick="showImageForm()">
                                    <i class="fas fa-camera"></i>
                                </button>
                            </div>
                        </div>
                        
                        <!-- Hidden image upload form -->
                        <div id="imageUploadForm" class="modal">
                            <div class="modal-content">
                                <span class="close" onclick="hideImageForm()">&times;</span>
                                <h2>Update Profile Picture</h2>
                                <form action="${pageContext.request.contextPath}/portfolio" method="post" enctype="multipart/form-data">
                                    <input type="hidden" name="action" value="updateImage">
                                    <div class="form-group">
                                        <label for="profile_image">Choose Image:</label>
                                        <input type="file" id="profile_image" name="profile_image" accept="image/*">
                                        <small>File must be less than 2MB. Accepted formats: jpg, jpeg, png</small>
                                    </div>
                                    <div class="preview-container">
                                        <img id="imagePreview" src="#" alt="Preview">
                                    </div>
                                    <div class="form-group">
                                        <button type="submit" class="action-btn">
                                            <i class="fas fa-upload"></i> Upload Image
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    
                    <div class="profile-details">
                        <!-- View Mode Card -->
                        <div id="viewCard" class="card">
                            <div class="card-header">
                                <i class="fas fa-user-circle"></i>
                                <h2>Personal Information</h2>
                                <button class="edit-btn" onclick="toggleEditMode(true)">
                                    <i class="fas fa-edit"></i> Edit
                                </button>
                            </div>
                            <div class="card-content">
                                <div class="info-row">
                                    <div class="info-label">
                                        <i class="fas fa-user"></i>
                                        <span>Name</span>
                                    </div>
                                    <div class="info-value">${user.user_name}</div>
                                </div>
                                
                                <div class="info-row">
                                    <div class="info-label">
                                        <i class="fas fa-envelope"></i>
                                        <span>Email</span>
                                    </div>
                                    <div class="info-value">${user.user_email}</div>
                                </div>
                                
                                <div class="info-row">
                                    <div class="info-label">
                                        <i class="fas fa-phone"></i>
                                        <span>Contact Number</span>
                                    </div>
                                    <div class="info-value">${user.contact_number}</div>
                                </div>
                                
                                <div class="info-row">
                                    <div class="info-label">
                                        <i class="fas fa-calendar-alt"></i>
                                        <span>Member Since</span>
                                    </div>
                                    <div class="info-value">${user.created_at}</div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Edit Mode Card (Initially Hidden) -->
                        <div id="editCard" class="card edit-card" style="display: none;">
                            <div class="card-header">
                                <i class="fas fa-user-edit"></i>
                                <h2>Edit Information</h2>
                                <button class="cancel-btn" onclick="toggleEditMode(false)">
                                    <i class="fas fa-times"></i> Cancel
                                </button>
                            </div>
                            <div class="card-content">
                                <form id="updateForm" action="${pageContext.request.contextPath}/portfolio" method="post">
                                    <input type="hidden" name="action" value="updateInfo">
                                    
                                    <div class="form-group">
                                        <label for="user_name">
                                            <i class="fas fa-user"></i> Name
                                        </label>
                                        <input type="text" id="user_name" name="user_name" value="${user.user_name}" 
                                               required placeholder="Enter your name">
                                    </div>
                                    
                                    <div class="form-group">
                                        <label for="user_email">
                                            <i class="fas fa-envelope"></i> Email
                                        </label>
                                        <input type="email" id="user_email" name="user_email" value="${user.user_email}" 
                                               required placeholder="Enter your email">
                                    </div>
                                    
                                    <div class="form-group">
                                        <label for="contact_number">
                                            <i class="fas fa-phone"></i> Contact Number
                                        </label>
                                        <input type="text" id="contact_number" name="contact_number" value="${user.contact_number}" 
                                               required placeholder="Enter your phone number">
                                    </div>
                                    
                                    <div class="form-group readonly">
                                        <label for="created_at">
                                            <i class="fas fa-calendar-alt"></i> Member Since
                                        </label>
                                        <input type="text" id="created_at" value="${user.created_at}" readonly>
                                        <small>This field cannot be edited</small>
                                    </div>
                                    
                                    <div class="form-actions">
                                        <button type="submit" class="action-btn save-btn">
                                            <i class="fas fa-save"></i> Save Changes
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                        
                        <div class="actions-container">
                            <button class="action-btn" onclick="window.location.href='${pageContext.request.contextPath}/products'">
                                <i class="fas fa-shopping-cart"></i> Continue Shopping
                            </button>
                            <button class="action-btn secondary" onclick="window.location.href='${pageContext.request.contextPath}/cart'">
                                <i class="fas fa-shopping-bag"></i> View Cart
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        
        <%@ include file="footer.jsp" %>
    </div>
    
    <script>
        // Toggle between view and edit modes
        function toggleEditMode(showEdit) {
            if (showEdit) {
                document.getElementById('viewCard').style.display = 'none';
                document.getElementById('editCard').style.display = 'block';
            } else {
                document.getElementById('viewCard').style.display = 'block';
                document.getElementById('editCard').style.display = 'none';
            }
        }
        
        // Image form functions
        function showImageForm() {
            document.getElementById('imageUploadForm').style.display = 'block';
        }
        
        function hideImageForm() {
            document.getElementById('imageUploadForm').style.display = 'none';
        }
        
        // Image preview
        $(document).ready(function() {
            $('#profile_image').change(function() {
                const file = this.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = function(e) {
                        $('#imagePreview').attr('src', e.target.result);
                        $('.preview-container').show();
                    }
                    reader.readAsDataURL(file);
                }
            });
            
            // Form validation
            $('#updateForm').submit(function(e) {
                const name = $('#user_name').val().trim();
                const email = $('#user_email').val().trim();
                const phone = $('#contact_number').val().trim();
                
                // Validate name (letters only)
                if (!/^[A-Za-z\s]+$/.test(name)) {
                    e.preventDefault();
                    Swal.fire({
                        icon: 'error',
                        title: 'Invalid Name',
                        text: 'Name should contain only letters',
                        confirmButtonColor: '#800040'
                    });
                    return;
                }
                
                // Validate email
                if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
                    e.preventDefault();
                    Swal.fire({
                        icon: 'error',
                        title: 'Invalid Email',
                        text: 'Please enter a valid email address',
                        confirmButtonColor: '#800040'
                    });
                    return;
                }
                
                // Validate phone number (10 digits, starts with 98)
                if (!/^98\d{8}$/.test(phone)) {
                    e.preventDefault();
                    Swal.fire({
                        icon: 'error',
                        title: 'Invalid Phone Number',
                        text: 'Phone number must be 10 digits and start with 98',
                        confirmButtonColor: '#800040'
                    });
                    return;
                }
            });
        });
        
        // Show background overlay when modal is open
        window.onclick = function(event) {
            const modal = document.getElementById('imageUploadForm');
            if (event.target == modal) {
                hideImageForm();
            }
        }
        
        // Check for success message in session
        <c:if test="${not empty sessionScope.success}">
            Swal.fire({
                icon: 'success',
                title: 'Success!',
                text: '${sessionScope.success}',
                confirmButtonColor: '#800040'
            });
            <% session.removeAttribute("success"); %>
        </c:if>
        
        // Check for error message in session
        <c:if test="${not empty sessionScope.error}">
            Swal.fire({
                icon: 'error',
                title: 'Error!',
                text: '${sessionScope.error}',
                confirmButtonColor: '#800040'
            });
            <% session.removeAttribute("error"); %>
        </c:if>
    </script>
</body>
</html>