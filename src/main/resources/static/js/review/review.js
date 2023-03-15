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