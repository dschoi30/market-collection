async function getReviewList({itemId, page, size}) {
    const result = await axios.get(`/reviews/${itemId}`, {params: {page, size}})

    return result.data;
}

async function getLikes(reviewId, itemId) {
    const result = await axios.patch(`/reviews/${reviewId}/likes`, {
        itemId: itemId
    })

    return result.data;
}