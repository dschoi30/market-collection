$(document).ready(function() {
    $("input[name=cartCheckBox]").change(function() {
        getTotalOrderPrice();
    });
});

// 장바구니 상품 수량 변경
function changeCount(obj) {
    const cartItemId = obj.dataset.id;
    const price = obj.dataset.price;
    const count = obj.value;
    const totalPrice = count * price;
    $("#count").val(count);
    $("#totalPrice").html(totalPrice.toLocaleString('en-US') + "원");
    getTotalOrderPrice();
    updateCartItemCount(cartItemId, count);
}

function updateCartItemCount(cartItemId, count) {

    const url = "/cart/" + cartItemId + "?count=" + count;

    $.ajax({
        url: url,
        type: "PATCH",
        dataType: "json",
        cache: false,
        success: function() {
            console.log("cartItem count update succeed");
        },
        error: function(jqXHR) {
            alert(jqXHR.responseText);
        }
    });
}

function getTotalOrderPrice() {
    let totalOrderPrice = 0;

    $(".cart-items").each(function(index, data) {
        if ($(data).find("#checkBox").is(":checked") === true) {
            let price = $(data).find("#price").val();
            let count = $(data).find("#count").val();
            totalOrderPrice += price * count;
        }
    });
    $("#totalOrderPrice").html(totalOrderPrice.toLocaleString('en-US') + '원');
    const pointSavingRate = $("#pointSavingRate").val();
    $("#savingPoint").html(Math.round(totalOrderPrice * pointSavingRate).toLocaleString('en-US') + '원');
}

function allChecked() {
    if($("#checkAll").prop("checked")) {
        $("input[name=cartCheckBox]").prop("checked", true);
    } else {
        $("input[name=cartCheckBox]").prop("checked", false);
    }
    getTotalOrderPrice();
}

function setOrderInfo() {
    let orderInfo = "";
    let no = 0;
    $(".cart-items").each(function(index, data) {
        if($(data).find("#checkBox").is(":checked") === true) {
            let itemId = $(data).find("#itemId").val();
            let count = $(data).find("#count").val();
            orderInfo += "<input type='number' name='orderItemRequests[" + no + "].itemId' value='" + itemId + "'>";
            orderInfo += "<input type='number' name='orderItemRequests[" + no + "].count' value='"+ count + "'>";
            no += 1;
        }
    })
    return orderInfo;
}

function order() {
    let form = $("<form action='/order' method='post'>" +
        setOrderInfo() +
        "</form> ");
    $("body").append(form);
    form.submit();
}

function deleteCartItem(obj) {
    const cartItemId = obj.dataset.id;
    const url = "/cart/" + cartItemId;

    $.ajax({
        url: url,
        type: 'DELETE',
        dataType: "json",
        cache: false,
        success: function(data) {
            location.reload();
        },
        error: function(error) {
            alert(error);
        }
    });
}
