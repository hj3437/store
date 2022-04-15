import { onClick } from "/js/clickListener.js"

export default function SearchResult({ $header, initState, onSearch }) {
    this.state = initState
    this.onSearch = onSearch

    const $page = document.createElement('div')
    $page.className = 'SearchResult'
    $header.querySelector('.header-container').appendChild($page)

    this.setState = (nextState) => {
        this.state = nextState
        this.render()
    }

    this.render = () => {        
        const { searchResult, searchKeyword, currentPage } = this.state
        let pageContents = ''
        if (searchResult) {
            const tempResult = searchResult.slice((currentPage - 1) * 5, currentPage * 5)            
            pageContents = `
            <div>
                <h3><i>${searchKeyword}</i> 검색결과 </h3>                
                <ul>                                         
                 ${tempResult.map(result => {
                    return `
                    <li data-store-id="${result.id}">
                        <span class="material-icons-outlined">search</span>                                                
                        <img class="searchImage" src="${result.imageUrl.indexOf('http') === 0 ? result.imageUrl : 'https://via.placeholder.com/40x40'}">
                        <span>${result.name}</span>
                    </li>                        
                    `
                }).join('')}
                    <li class="searchClose">          
                        <span>${searchResult.length}건 검색됨</span>                  
                        <span class="search-prev-button">이전</span>
                        <span>${this.state.currentPage}/${Math.ceil(searchResult.length / 5) === 0 ? 1 : Math.ceil(searchResult.length / 5)}</span>
                        <span class="search-next-button">다음</span>
                        <span class="search-close-button">닫기</span>
                    </li>                        
                <ul>               
            </div>
            `
            $page.style.border = 'solid 1px grey'
        } else {
            pageContents = ``
            $page.style.border = 'none'
        }
        $page.innerHTML = pageContents
    }

    this.render()

    $page.addEventListener('click', (e) => {
        const li = e.target.closest('li')
        if (li) {
            const { storeId } = li.dataset
            if (storeId) {
                onClick(`/restaurants/${storeId}`)
            } else {
                if (e.target.closest('.search-close-button')) {
                    $page.innerHTML = ``
                    $page.style.border = 'none'
                }
            }
        }

        if (e.target.closest('.search-next-button')) {
            const { currentPage, searchResult } = this.state
            const maxIndex = Math.ceil(searchResult.length / 5)

            if (searchResult.length !== 0 && maxIndex > currentPage) {
                this.setState({ ...this.state, currentPage: currentPage + 1 })
            } else {
                alert('마지막 페이지입니다.')
            }
            return
        }

        if (e.target.closest('.search-prev-button')) {            
            const { currentPage, searchResult } = this.state
            const maxIndex = Math.ceil(searchResult.length / 5)

            if (currentPage >= 2 && currentPage <= maxIndex) {
                this.setState({ ...this.state, currentPage: currentPage - 1 })
            } else {
                alert('첫번쨰 페이지입니다.')
            }
            return
        }
    })
}