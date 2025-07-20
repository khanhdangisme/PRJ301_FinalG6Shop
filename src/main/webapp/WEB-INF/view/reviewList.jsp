<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="model.User" %>
<%
    User loggedUser = (User) session.getAttribute("loggedUser");
%>
<div class="container my-4">
    <h4 class="mb-3">Product Reviews</h4>
    <c:choose>
        <c:when test="${empty reviews}">
            <p class="text-muted">No reviews yet. Be the first to review this product!</p>
        </c:when>
        <c:otherwise>
            <ul class="list-group mb-4">
                <c:forEach var="r" items="${reviews}">
                    <li class="list-group-item d-flex justify-content-between align-items-start flex-column flex-md-row">
                        <div>
                            <span class="fw-bold">Rating: </span>
                            <span style="color: #ffc107;">
                                <c:forEach begin="1" end="5" var="i">
                                    <i class="fa fa-star${i <= r.rating ? '' : '-o'}"></i>
                                </c:forEach>
                            </span>
                            <span class="ms-2">${r.comment}</span>
                            <div class="small text-muted mt-1">
                                <fmt:formatDate value="${r.reviewDate}" pattern="dd/MM/yyyy HH:mm" />
                            </div>
                        </div>
                        <c:if test="${loggedUser != null && loggedUser.userID == r.userId}">
                            <div class="mt-2 mt-md-0">
                                <form action="review" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="delete" />
                                    <input type="hidden" name="id" value="${r.id}" />
                                    <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                </form>
                                <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#editReviewModal${r.id}">Edit</button>
                            </div>
                        </c:if>
                    </li>
                    <!-- Edit Modal -->
                    <div class="modal fade" id="editReviewModal${r.id}" tabindex="-1" aria-labelledby="editReviewModalLabel${r.id}" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form action="review" method="post">
                                    <input type="hidden" name="action" value="edit" />
                                    <input type="hidden" name="id" value="${r.id}" />
                                    <div class="modal-header bg-primary text-white py-2 px-3 align-items-center">
                                        <h5 class="modal-title mb-0" id="editReviewModalLabel${r.id}" style="font-size: 1.1rem;">Edit Review</h5>
                                        <button type="button" class="btn-close btn-close-white ms-auto" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body pt-3 pb-2 px-3">
                                        <div class="mb-2">
                                            <label>Rating:</label>
                                            <input type="number" class="form-control" name="rating" min="1" max="5" value="${r.rating}" required />
                                        </div>
                                        <div class="mb-2">
                                            <label>Comment:</label>
                                            <textarea class="form-control" name="comment" rows="3" required>${r.comment}</textarea>
                                        </div>
                                    </div>
                                    <div class="modal-footer py-2 px-3">
                                        <button type="submit" class="btn btn-primary">Update</button>
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </ul>
        </c:otherwise>
    </c:choose>
    <!-- Add Review Form -->
    <c:if test="${loggedUser != null}">
        <div class="card mb-3">
            <div class="card-body">
                <h5 class="card-title">Add Your Review</h5>
                <form action="review" method="post">
                    <input type="hidden" name="action" value="add" />
                    <input type="hidden" name="productId" value="${param.productId != null ? param.productId : productId}" />
                    <div class="mb-2">
                        <label>Rating:</label>
                        <input type="number" class="form-control" name="rating" min="1" max="5" required />
                    </div>
                    <div class="mb-2">
                        <label>Comment:</label>
                        <textarea class="form-control" name="comment" rows="3" required></textarea>
                    </div>
                    <button type="submit" class="btn btn-success">Submit Review</button>
                </form>
            </div>
        </div>
    </c:if>
    <c:if test="${loggedUser == null}">
        <div class="alert alert-info">Please <a href="login">login</a> to add a review.</div>
    </c:if>
</div> 