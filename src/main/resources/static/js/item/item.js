var item = {
    init: function() {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        })
        $('#btn-update').on('click', function () {
            _this.update();
        })
        $('#btn-delete').on('click', function () {
            _this.delete();
        })
    },
    save: function() {
        const formData = new FormData();
        const data = {
            itemSaleStatus: $('#itemSaleStatus').val(),
            category: $('#category').val(),
            itemName: $('#itemName').val(),
            originalPrice: $('#originalPrice').val(),
            salePrice: $('#salePrice').val(),
            stockQuantity: $('#stockQuantity').val(),
            description: $('#description').val()
        };

        const fileInput = $('.custom-file-input');

        for (let i = 0; i < fileInput.length; i++) {
            if (fileInput[i].files.length > 0) {
                for (let j = 0; j < fileInput[i].files.length; j++) {
                    console.log("fileInput[i].files[j] = " + fileInput[i].files[j]);

                    // formData에 'file'이라는 키값으로 fileInput 값을 append 시킨다.
                    formData.append('file', $('.custom-file-input')[i].files[j]);
                }
            }
        }

        formData.append('key', new Blob([JSON.stringify(data)], {type: "application/json"}));

        $.ajax({
            type: 'POST',
            url: '/api/v1/admin/item/new',
            data: formData,
            contentType: false,
            processData: false,
            enctype: 'multipart/form-data',
        }).done(function () {
            alert('상품이 등록되었습니다.');
            window.location.href = '/';
        }).fail(function (jqXHR) {
            alert(jqXHR.responseText);
        });
    },
    update: function() {
        const formData = new FormData();

        let itemImageIds = new Array();
        $('.item-image-ids').each(function(index, item) {
            const itemImageId = $(item).val();
            itemImageIds.push(itemImageId);
        });

        const data = {
            id: $('#id').val(),
            itemSaleStatus: $('#itemSaleStatus').val(),
            category: $('#category').val(),
            itemName: $('#itemName').val(),
            originalPrice: $('#originalPrice').val(),
            salePrice: $('#salePrice').val(),
            stockQuantity: $('#stockQuantity').val(),
            description: $('#description').val(),
            itemImageIds: itemImageIds
        };

        const fileInput = $('.custom-file-input');

        for (let i = 0; i < fileInput.length; i++) {
            if (fileInput[i].files.length > 0) {
                for (let j = 0; j < fileInput[i].files.length; j++) {
                    console.log("fileInput[i].files[j] = " + fileInput[i].files[j]);

                    // formData에 'file'이라는 키값으로 fileInput 값을 append 시킨다.
                    formData.append('file', $('.custom-file-input')[i].files[j]);
                }
            }
        }

        formData.append('key', new Blob([JSON.stringify(data)], {type: "application/json"}));

        $.ajax({
            type: 'POST',
            url: '/api/v1/admin/item/' + data.id,
            data: formData,
            contentType: false,
            processData: false,
            enctype: 'multipart/form-data',
        }).done(function () {
            alert('상품이 정상적으로 수정되었습니다.');
            window.location.href = '/items/' + data.id;
        }).fail(function (error) {
            alert(JSON.stringify(error))
        });
    }
};

item.init();
