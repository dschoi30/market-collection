var review = {
    init: function() {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        })
    },
    save: function() {
        const url = "/review"
        const paramData = {
            itemId: $('#itemId').val(),
            content: $('#content').val()
        }
        const param = JSON.stringify(paramData);
        console.log(param);

        $.ajax({
            url: url,
            type: 'POST',
            contentType: "application/json",
            data: param,
            dataType: "json",
            cache: false,
            success: function () {
                alert("상품 리뷰가 등록되었습니다.");
                window.location.href = '/orders';
            },
            error: function (jqXHR, status, error) {
                alert(jqXHR.responseText);
            }
        });
    }
};

review.init();
