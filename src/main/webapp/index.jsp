<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Аренда фототехники PhotoGear</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<c:if test="${empty rentals and empty param.redirected}">
    <c:redirect url="/rentals?redirected=true"/>
</c:if>
<h1>Записи об аренде фототехники PhotoGear</h1>

<!-- Форма добавления -->
<div class="form-section">
    <h2>Добавить запись об аренде фототехники</h2>
    <form action="rentals" method="post">
        <table class="non-border-table">
            <tr>
                <td>Имя клиента</td>
                <td>Телефон</td>
                <td>Дата аренды</td>
                <td>Оборудование</td>
                <td>Цена</td>
                <td>Длительность</td>
            </tr>
            <tr>
                <td><input type="text" name="clientName" required></td>
                <td><input type="text" name="phoneNumber" required></td>
                <td><input type="date" name="rentalDate" required></td>
                <td><input type="text" name="itemName" required></td>
                <td><input type="number" step="0.01" name="price" required></td>
                <td><input type="text" name="duration" required></td>
            </tr>
            <tr>
                <td colspan="7">
                    <button type="submit" class="btn add-btn">Добавить запись об аренде</button>
                </td>
            </tr>
        </table>
    </form>
</div>

<c:if test="${not empty editRental}">
    <div class="form-section">
        <form action="${pageContext.request.contextPath}/rentals" method="post">
            <table class="non-border-table">
                <tr>
                    <td colspan="7">
                        <h2>Изменить запись</h2>
                    </td>
                </tr>
                <tr>
                    <td>Имя клиента</td>
                    <td>Телефон</td>
                    <td>Дата аренды</td>
                    <td>Оборудование</td>
                    <td>Цена</td>
                    <td>Длительность</td>
                    <td><button type="submit" class="btn add-btn">Применить</button></td>
                </tr>
                <tr>
                    <td>
                        <input type="hidden" name="_method" value="PUT">
                        <input type="hidden" name="id" value="${editRental.id}">
                        <input type="text" name="clientName" value="${editRental.clientName}" required>
                    </td>
                    <td><input type="text" name="phoneNumber" value="${editRental.phoneNumber}" required></td>
                    <td><input type="date" name="rentalDate" value="${editRental.rentalDate}" required></td>
                    <td><input type="text" name="itemName" value="${editRental.itemName}" required></td>
                    <td><input type="number" step="0.01" name="price" value="${editRental.price}" required></td>
                    <td><input type="text" name="duration" value="${editRental.duration}" required></td>
                    <td><a href="${pageContext.request.contextPath}/rentals"><button class="btn">Отмена</button></a></td>
                </tr>
            </table>
        </form>
    </div>
</c:if>

<div class="form-section">
    <h2>Данные о записях</h2>
    <table>
        <tr>
            <th class="bordered-cells th">ID</th>
            <th class="bordered-cells th">Клиент</th>
            <th class="bordered-cells th">Телефон</th>
            <th class="bordered-cells th">Дата</th>
            <th class="bordered-cells th">Оборудование</th>
            <th class="bordered-cells th">Цена</th>
            <th class="bordered-cells th">Длительность</th>
            <th class="bordered-cells th" colspan="2">Действия</th>
        </tr>
        <c:choose>
            <c:when test="${empty rentals}">
                <tr>
                    <td colspan="9" style="text-align: center;" class="bordered-cells">
                        Здесь пока нет записей
                    </td>
                </tr>
            </c:when>
            <c:otherwise>
            <c:forEach items="${rentals}" var="rental">
                <tr>
                    <td class="bordered-cells">${rental.id}</td>
                    <td class="bordered-cells">${rental.clientName}</td>
                    <td class="bordered-cells">${rental.phoneNumber}</td>
                    <td class="bordered-cells">${rental.rentalDate}</td>
                    <td class="bordered-cells">${rental.itemName}</td>
                    <td class="bordered-cells">${rental.price}</td>
                    <td class="bordered-cells">${rental.duration}</td>
                    <td class="bordered-cells"><a href="rentals?action=edit&id=${rental.id}"><button class="btn">Изменить запись</button></a></td>
                    <td class="bordered-cells">
                        <form action="rentals" method="post" style="display:inline;">
                            <input type="hidden" name="_method" value="DELETE">
                            <input type="hidden" name="id" value="${rental.id}">
                            <button type="submit" class="btn btn-delete">Удалить запись</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </c:otherwise>
        </c:choose>
    </table>
</div>
</body>
</html>