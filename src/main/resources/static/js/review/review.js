async function getReviewList({itemId, page, size, goLast}) {
    const result = await axios.get(`/reviews/${itemId}`, {params: {page, size}})
    console.log(result);

    if(goLast){
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total/size))

        return getReviewList({itemId:itemId, page:lastPage, size:size})
    }

    return result.data;
}

async function getLikes(reviewId, itemId) {
    const result = await axios.patch(`/reviews/${reviewId}/likes`, {
        itemId: itemId
    })

    return result.data;
}