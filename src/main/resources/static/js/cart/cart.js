$(document).ready(function() {
    $("input[name=cartCheckBox]").change(function() {
        getTotalOrderPrice();
    });
});

// 장바구니 상품 수량 변경
function changeCount(obj) {
    const count = obj.value;
    const cartItemId = obj.dataset.id;
    const price = $("#price_" + cartItemId).data("price");
    const totalPrice = count * price;
    $("#totalPrice_" + cartItemId).html(totalPrice + "원");
    getTotalOrderPrice();
    updateCartItemCount(cartItemId, count);
}

function updateCartItemCount(cartItemId, count) {

    const url = "/cartItem/" + cartItemId + "?count=" + count;

    $.ajax({
        url: url,
        type: "PATCH",
        dataType: "json",
        cache: false,
        success: function() {
            console.log("cartItem count update succeed");
        },
        error: function(error) {
            alert(error);
        }
    });
}

function getTotalOrderPrice() {
    let totalOrderPrice = 0;
    $("input[name=cartCheckBox]:checked").each(function () {
        const cartItemId = $(this).val();
        const price = $("#price_" + cartItemId).attr("data-price");
        const count = $("#count_" + cartItemId).val();
        totalOrderPrice += price * count;
    });

    $("#totalOrderPrice").html(totalOrderPrice + '원');
}

function allChecked() {
    /*
                $("#checkAll").click(function(){
                    $('input:checkbox').not(this).prop('checked', this.checked);
                });
    */
    if($("#checkAll").prop("checked")) {
        $("input[name=cartCheckBox]").prop("checked", true);
    } else {
        $("input[name=cartCheckBox]").prop("checked", false);
    }
    getTotalOrderPrice();
}

function orderInfo() {
    let orderInfo = "";
    let cartItems = $(".cart-items");
    for(let i = 0; i < cartItems.length; i++) {
        let isSelected = $(cartItems[i]).find("#checkBox").is(":checked");
        if(isSelected) {
            let itemId = $(cartItems[i]).find("#itemId").val();
            let count = $(cartItems[i]).find("#count").val();
            orderInfo += "<input type='number' name='orderItemRequestDtos[" + i + "].itemId' value='" + itemId + "'>";
            orderInfo += "<input type='number' name='orderItemRequestDtos[" + i + "].count' value='"+ count + "'>";
            console.log(orderInfo);
        }
    }
    return orderInfo;
}

function order() {
    let form = $("<form action='/order' method='post'>" +
        orderInfo() +
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
