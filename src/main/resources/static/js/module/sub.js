{
    let view = {
        el: '#main-wrapper',
        template: `
        <div class="main-sub">

        </div>
        <div class="main-ans-list">
        </div>`,
        render(data) {
            $.el(this.el).innerHTML = $.evalTemplate(this.template, data)
        }
    }

    let model = {}

    let controller = {
        init(view, model) {
            this.view = view
            this.model = model
            this.bindEvents()
            this.bindEventHub()
        },
        bindEvents() {
            $.bindEvent(".main-ans-list .ans", "dblclick", (e, from) => {
                if (e.shiftKey) {
                    let target = from.querySelector(".ans-desc")
                    let ansId = from.dataset.id
                    let ansData = this.model.sub.ansList.filter(x => x.ansId === ansId)[0]

                    target.textContent = ansData.ansDesc
                    target.contentEditable = true
                    target.style.whiteSpace = 'pre-line'

                    let editBtn = document.createElement("button")
                    editBtn.innerText = "保存"
                    editBtn.classList.add("ans-save-btn")
                    from.append(editBtn)

                    // 绑定编辑方法
                    target.onkeydown = (e) => this.edit.call(this, e)

                    editBtn.onclick = () => {
                        let ansDesc = target.textContent

                        $.request("https://my.snowtraces.com/qa/api/v1/ans", {ansId, ansDesc}, "PUT")
                            .then((res) => {
                                if (res.success) {
                                    target.innerHTML = ansDesc
                                    target.contentEditable = false
                                    target.style.whiteSpace = 'inherit'
                                    editBtn.remove()

                                    ansData.ansDesc = ansDesc
                                    Prism.highlightAll()
                                }
                            })
                    }
                }
            })

            $.bindEvent(".main-sub", "dblclick", (e, from) => {
                if (e.shiftKey) {
                    // 标题
                    let title = from.querySelector(".sub-title h1")
                    title.contentEditable = true

                    // 内容
                    let target = from.querySelector(".sub-desc")
                    let subId = target.dataset.id
                    let subData = this.model.sub

                    target.textContent = subData.subDesc
                    target.contentEditable = true
                    target.style.whiteSpace = 'pre-line'

                    let editBtn = document.createElement("button")
                    editBtn.innerText = "保存"
                    editBtn.classList.add("sub-save-btn")
                    from.append(editBtn)

                    // 绑定编辑方法
                    target.onkeydown = (e) => this.edit.call(this, e)

                    editBtn.onclick = () => {
                        let subDesc = target.textContent
                        let subTitle = title.textContent

                        $.request("https://my.snowtraces.com/qa/api/v1/sub", {subId, subDesc, subTitle}, "PUT")
                            .then((res) => {
                                if (res.success) {
                                    target.innerHTML = subDesc
                                    target.contentEditable = false
                                    target.style.whiteSpace = 'inherit'
                                    title.contentEditable = false
                                    editBtn.remove()

                                    subData.subDesc = subDesc
                                    Prism.highlightAll()
                                }
                            })
                    }
                }
            })

        },
        bindEventHub() {
            window.eventHub.on(routes.sub.event, ({value, loadView}) => {
                if (loadView) {
                    this.view.render(this.model.data)
                }
                this.onload(value)
            })
        },
        onload(subId) {
            $.get("https://my.snowtraces.com/qa/api/v1/sub", {subId}).then((res) => {
                let sub = res.data || []
                this.model.sub = sub
                $.el(".main-sub").innerHTML = `
                    <div class="sub-title" data-id="${sub.subId}"><h1>${sub.subTitle}</h1></div>
                    <div class="sub-desc" data-id="${sub.subId}">${sub.subDesc}</div>
                    <div class="author">${sub.addByName}</div>`

                if (sub.ansList.length) {
                    $.el(".main-ans-list").innerHTML = sub.ansList.map(ans => `
                        <div class="ans ${ans.accepted === '1' ? 'accepted' : ''}" data-id="${ans.ansId}">
                            <div class="ans-desc">${ans.ansDesc}</div>
                            <div class="author">${ans.addByName}</div>
                        </div>
                       `).join("\n")
                }
                Prism.highlightAll()
                this.resetChineseStyle()

            })

        },
        resetChineseStyle() {
            $.elAll(".ans-desc p em, .sub-desc p em").forEach(emItem => {
                if (emItem.innerText && /[\u4e00-\u9fa5]/.test(emItem.innerText)) {
                    emItem.setAttribute("lang", "zh")
                }
            })
        },
        edit: function (e) {
            let keyToReplace = {
                '`': ['<code>', '</code>'],
                'b': ['<strong>', '</strong>']
            }
            let toReplace = keyToReplace[e.key];
            if (toReplace) {
                this.replaceSelection(e, ...toReplace)
            }
        },
        replaceSelection(e, start, end) {
            let sel = window.getSelection();
            if (sel.rangeCount) {
                let range = sel.getRangeAt(0);
                let fragment = range.extractContents();
                let oldContent = fragment.textContent
                if (oldContent) {
                    e.preventDefault()
                    if (oldContent.startsWith(start) && oldContent.endsWith(end)) {
                        fragment.textContent = fragment.textContent.substring(start.length, oldContent.length - end.length)
                    } else {
                        fragment.textContent = `${start}${oldContent}${end}`
                    }
                }
                range.insertNode(fragment);
            }
        }
    }

    controller.init(view, model)
}
