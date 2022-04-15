const CLICK_EVENT = 'CLICK_EVENT'

export const initClickEvent = (clickListener) => {
    window.addEventListener(CLICK_EVENT, clickListener)
}

export const onClick = (url, param) => {    
    history.pushState(null, null, url)
    dispatchEvent(new CustomEvent(CLICK_EVENT, param))
}